package com.example.authserver.config;

import com.example.authserver.model.User;
import com.example.authserver.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

// Novos imports para configurar o JWT Authentication Converter
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Esta anotação já está correta e é essencial.
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // PasswordEncoder: Pra codificar senhas de forma segura
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // UserDetailsService: Como o Spring Security vai carregar os detalhes do usuário
    // OBS: Para a validação de JWT, o UserDetailsService não é diretamente usado
    // para extrair as roles do token, mas sim para outras formas de autenticação
    // ou se você estivesse usando um modelo de autenticação diferente.
    // Para JWT com @PreAuthorize, o crucial é o JwtAuthenticationConverter.
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole()) // A role aqui será "ADMIN" ou "USER"
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    // JwtDecoder: O componente que o Spring Security usa pra decodificar e validar JWTs
    @Bean
    public JwtDecoder jwtDecoder() {
        // A chave secreta é convertida pra um SecretKeySpec para HMAC
        SecretKeySpec secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSha256");
        // Constrói o NimbusJwtDecoder com a chave secreta. Ele fará a validação da assinatura.
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    /**
     * Configura um JwtAuthenticationConverter para extrair as roles da claim "role" do JWT.
     * Isso é essencial para que o Spring Security mapeie corretamente as autoridades
     * e o @PreAuthorize funcione.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Cria um conversor padrão de GrantedAuthorities que pode ser configurado
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Define que as authorities devem ser extraídas da claim "role" (sua claim personalizada)
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        // Prefixo para as roles. O Spring Security espera "ROLE_" para usar com hasRole().
        // Se sua role no token for "ADMIN", ela se tornará "ROLE_ADMIN".
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }

    // SecurityFilterChain: Configura as regras de segurança HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF pra APIs RESTful stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API não manterá estado de sessão
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll() // Permite acesso público ao login
                .requestMatchers("/auth/validate").permitAll() // Permite acesso público ao endpoint de validação
                .requestMatchers("/h2-console/**").permitAll() // Console H2
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Documentação Swagger
                .requestMatchers("/actuator/**").permitAll() // libera o Actuator
                .anyRequest().authenticated() // Qualquer outra requisição exige um JWT válido
            )
            .headers(headers -> headers.frameOptions(frameOptions -> headers.frameOptions().sameOrigin())) // Necessário para o H2 console
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                // Ao chamar .jwt(), o Spring Security usará o JwtDecoder que definimos como um Bean.
                // Agora, também configuramos o AuthenticationConverter para lidar com suas roles personalizadas.
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()); // <<< Adicione esta linha
            }));

        return http.build();
    }

    // CommandLineRunner: Popula o banco de dados H2 com usuários iniciais ao iniciar a aplicação
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User(null, "admin", passwordEncoder.encode("123456"), "ADMIN");
                userRepository.save(admin);
                System.out.println("✅ Usuário 'admin' criado com senha codificada.");
            }
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User(null, "user", passwordEncoder.encode("password"), "USER");
                userRepository.save(user);
                System.out.println("✅ Usuário 'user' criado com senha codificada.");
            }
        };
    }
}