README - Servidor de Autenticação JWT
Este repositório contém um servidor de autenticação Spring Boot com JWT para gerenciamento de usuários, login, geração e validação de tokens. Ele utiliza um banco de dados H2 em memória, Spring Security para controle de acesso, e documentação OpenAPI (Swagger UI). Também inclui configurações para testes de carga com JMeter.

Siga os passos abaixo para clonar, configurar e executar o projeto

Clonando o Repositório
Para obter uma cópia local do projeto, execute o seguinte comando no seu terminal:

Bash

git clone [URL_DO_SEU_REPOSITORIO]
cd [nome-do-seu-repositorio]


📦 Dependências do Projeto
As dependências essenciais do projeto estão configuradas no arquivo pom.xml, garantindo que tudo funcione perfeitamente com as versões mais recentes do Spring Boot 3.x.

spring-boot-starter-web: Para construir APIs RESTful.

spring-boot-starter-security: O core da segurança do Spring, para autenticação e autorização.

spring-boot-starter-oauth2-resource-server: Permite que a API valide tokens JWT, agindo como um Servidor de Recursos.

spring-boot-starter-data-jpa: Para persistência de dados usando JPA.

com.h2database:h2: Banco de dados em memória, perfeito para desenvolvimento e testes.

com.auth0:java-jwt: Biblioteca para gerar e validar JWTs programaticamente.

org.springdoc:springdoc-openapi-starter-webmvc-ui: Gera a documentação automática da API com Swagger UI.

org.springframework.boot:spring-boot-devtools: Ferramentas que aceleram o desenvolvimento, como "hot reload".

org.projectlombok:lombok: Reduz código repetitivo (getters, setters, etc.).

org.springframework.boot:spring-boot-starter-test: Inclui JUnit 5 e Mockito, essenciais para testes.

⚙️ Configuração do Ambiente de Desenvolvimento
O arquivo src/main/resources/application.yml controla o comportamento da API, incluindo configurações do servidor, banco de dados H2, JWT e Swagger.

application.yml
YAML

# 🚀 Configurações do Servidor Web
server:
  port: 8080 # Porta padrão para a sua API

# 🗄️ Configurações do Banco de Dados H2
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE # H2 em memória, não fecha o banco. Útil para desenvolvimento.
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true # Habilita o console do H2
      path: /h2-console # Acesse em http://localhost:8080/h2-console
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update # Cria/atualiza o schema do DB automaticamente. **NÃO USE EM PRODUÇÃO!**
    show-sql: true # Exibe as queries SQL no console (ótimo para depuração)
    properties:
      hibernate:
        format_sql: true # Formata o SQL para melhor legibilidade no console

# 🛠️ Spring DevTools: Otimizando o Desenvolvimento
  devtools:
    restart:
      enabled: true # Reinicia a app automaticamente ao detectar mudanças no código
    livereload:
      enabled: true # Habilita o LiveReload (requer extensão no navegador)

# 🔒 Configurações JWT para Geração e Validação Interna
# IMPORTANTE: EM PRODUÇÃO, A CHAVE SECRETA DEVE SER UMA VARIÁVEL DE AMBIENTE OU GERENCIADA POR UM SERVIÇO DE SECRETS!
jwt:
  secret: umaChaveSecretaMuitoLongaEComplexaParaAssinarTokensJWT # Use uma string longa, aleatória e segura (mínimo de 32 caracteres para HMAC256).
  expiration: 3600000 # Tempo de expiração do token em milissegundos (aqui: 1 hora)

# 📚 Springdoc OpenAPI (Swagger): Documentação da API
springdoc:
  swagger-ui:
    path: /swagger-ui.html # Caminho para a interface do Swagger UI
    disable-swagger-default-url: true # Evita carregar a URL padrão do Swagger (Petstore)
  api-docs:
    path: /v3/api-docs # Caminho para os arquivos de definição da API (JSON/YAML)
▶️ Executando a Aplicação
Para iniciar o servidor de autenticação, você pode usar o Maven:

Bash

mvn spring-boot:run
A aplicação será iniciada na porta 8080 (ou na porta configurada no application.yml).

🌐 Acessando o Console H2
Com a aplicação em execução, você pode acessar o console do banco de dados H2 para visualizar os dados em memória:

URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

Username: sa

Password: (deixe em branco)

Ao iniciar a aplicação, dois usuários de exemplo serão criados automaticamente:

admin: username: admin, password: 123456, role: ADMIN

user: username: user, password: password, role: USER

📖 Acessando a Documentação Swagger UI
A documentação interativa da API, gerada automaticamente pelo Springdoc OpenAPI (Swagger UI), pode ser acessada em:

URL: http://localhost:8080/swagger-ui.html

Nesta interface, você poderá:

Visualizar todos os endpoints disponíveis.

Testar os endpoints diretamente no navegador.

Entender os parâmetros de requisição e as respostas esperadas.

Para testar endpoints protegidos, como /api/hello ou /api/admin, você precisará de um token JWT. Siga estes passos:

No Swagger UI, encontre o endpoint /auth/login.

Preencha username e password (ex: admin e 123456).

Execute a requisição (Try it out -> Execute).

Copie o token JWT retornado na resposta.

Clique no botão "Authorize" no topo da página do Swagger UI.

No campo Value, insira Bearer <SEU_TOKEN_JWT> (substituindo <SEU_TOKEN_JWT> pelo token copiado).

Clique em Authorize e depois em Close.

Agora, você pode testar os endpoints protegidos.

✅ Executando os Testes JUnit
Os testes de integração e unidade para a API de autenticação estão localizados em src/test/java/com/example/authserver/AuthIntegrationTests.java. Você pode executá-los usando o Maven:

Bash

mvn test
Este comando compilará e executará todos os testes JUnit, garantindo que a lógica de autenticação, geração/validação de tokens e proteção de endpoints estejam funcionando corretamente.

📈 Executando Testes de Carga com JMeter
Para realizar testes de desempenho e carga na API, você pode usar o Apache JMeter.

1. Instalação do JMeter
Se você ainda não tem o JMeter instalado:

Baixe a versão mais recente em Apache JMeter Downloads.

Descompacte o arquivo ZIP/tar.gz em um diretório de sua escolha.

Execute o JMeter:

No Windows: Vá para o diretório bin dentro da pasta do JMeter e execute jmeter.bat.

No Linux/macOS: Vá para o diretório bin e execute jmeter.sh.

2. Abrindo o Plano de Teste JMeter
Neste repositório, você deve salvar seu plano de teste JMeter (o arquivo .jmx) em uma pasta como jmeter-tests/.

Abra o JMeter.

Vá em File > Open e navegue até o local onde você salvou o arquivo .jmx (ex: jmeter-tests/login_stress_test.jmx).

3. Configuração do Plano de Teste (Exemplo: Teste de Login)
O arquivo .jmx de exemplo incluirá um plano de teste para o endpoint de login. Se precisar criar um novo ou modificar o existente:

Thread Group:

Clique direito em Test Plan > Add > Threads (Users) > Thread Group.

Number of Threads (users): (Ex: 200 para simular 200 usuários simultâneos).

Ramp-up period (seconds): (Ex: 20 para que os 200 usuários iniciem ao longo de 20 segundos).

Loop Count: (Ex: 10 para cada usuário fazer 10 requisições de login, ou Forever para teste contínuo).

HTTP Request (Login):

Clique direito no Thread Group > Add > Sampler > HTTP Request.

Name: Login Request

Protocol: http

Server Name or IP: localhost

Port Number: 8080

Method: POST

Path: /auth/login

Parameters: Na seção "Parameters", clique em "Add" e adicione:

username: admin

password: 123456

Listeners (Visualização de Resultados):

Clique direito no Thread Group > Add > Listener > View Results Tree (para ver detalhes de cada requisição).

Clique direito no Thread Group > Add > Listener > Summary Report (para um resumo das métricas).

4. Executando o Teste de Carga
Com o plano de teste carregado no JMeter, clique no botão verde "Start" (ou Ctrl + R).

Deixe o teste rodar até que as requisições sejam concluídas ou o tempo configurado expire.

5. Analisando os Relatórios
Após a execução, verifique os Listeners que você adicionou:

Summary Report: Apresenta métricas consolidadas como Average (Tempo Médio de Resposta), Error % (Porcentagem de Erros), Throughput (Requisições por segundo), etc., fornecendo uma visão geral do desempenho.

View Results Tree: Permite inspecionar cada requisição individualmente, visualizando o tempo de resposta, o status HTTP, e os dados de requisição e resposta para depuração detalhada.
