# 🔐 Projeto de Autenticação com Spring Boot

Este projeto implementa uma API de autenticação JWT utilizando Spring Boot 3.x, com recursos de segurança, documentação, testes, monitoramento e deploy em nuvem.

---

## 📦 Dependências Utilizadas

O projeto usa as seguintes bibliotecas no `pom.xml`:

- 🌐 **spring-boot-starter-web** – Construção de APIs REST.
- 🔒 **spring-boot-starter-security** – Autenticação e autorização.
- 🔑 **spring-boot-starter-oauth2-resource-server** – Validação de tokens JWT.
- 🗄️ **spring-boot-starter-data-jpa** – Acesso a dados com JPA.
- 💾 **h2-database** – Banco de dados em memória.
- 📚 **springdoc-openapi-ui** – Documentação automática com Swagger UI.
- 🛠️ **spring-boot-devtools** – Ferramentas para desenvolvimento.
- 🍬 **lombok** – Redução de código repetitivo com anotações.
- ✅ **spring-boot-starter-test** – Testes com JUnit e Mockito.
- 🩺 **spring-boot-starter-actuator** – Métricas e monitoramento.
- 📊 **Prometheus** – Coleta de métricas em tempo real.

---

## ⚙️ Configuração

No `application.yml`, foram configuradas:

- 📁 Conexões com o banco H2
- 🔐 Configurações de segurança e JWT
- 🩺 Ativação dos endpoints do Spring Boot Actuator

---

## 🛡️ Funcionalidades da API

- `POST /auth/register` – Cadastro de novos usuários
- `POST /auth/login` – Login e geração de token JWT
- Endpoints protegidos com autenticação JWT
- Controle de acesso por perfil (usuário e admin)

---

## 🧪 Testes

- Testes unitários com JUnit e Mockito
- Teste de carga com **Apache JMeter**
  - Simulação de múltiplos logins simultâneos
  - Métricas: Throughput, Tempo médio, % de erro

---

## 📖 Documentação

A documentação da API está disponível via Swagger:

http://localhost:8080/swagger-ui.html

yaml
Copiar
Editar

---

## 📊 Monitoramento

### Métricas expostas com Spring Boot Actuator:
- `/actuator/health`
- `/actuator/metrics`
- `/actuator/prometheus`

### Monitoramento com Prometheus e Grafana:

- Prometheus coleta dados do endpoint `/actuator/prometheus`
- Grafana exibe dashboards com:
  - Requisições HTTP
  - Uso de memória
  - Tempo de resposta
  - Status da aplicação

---

## 🐳 Docker

### Construção da imagem:

```bash
docker build -t minha-api .
Execução local:
bash
Copiar
Editar
docker run -p 8080:8080 minha-api
🌐 Deploy em Nuvem
🚀 Render (https://render.com)
Plataforma detecta o Dockerfile automaticamente.

Build feito diretamente no container.

URL pública é gerada ao final.

🛠️ Railway (https://railway.app)
Suporte completo a projetos com Dockerfile.

Integração direta com GitHub.

💻 Execução Local (sem Docker)
bash
Copiar
Editar
mvn clean package
mvn spring-boot:run
Acesse a aplicação em:

arduino
Copiar
Editar
http://localhost:8080
📂 Estrutura do Projeto
css
Copiar
Editar
Autenticacao/
├── src/
├── target/
├── Dockerfile
├── pom.xml
├── application.yml
├── prometheus.yml
└── README.md ✅
