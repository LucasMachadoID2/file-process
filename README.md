# File Process API

API para processamento de arquivos com autenticação JWT.

## 🚀 Tecnologias

- Spring Boot 3.x
- Spring Security com JWT
- Spring Data JPA
- PostgreSQL
- Flyway (migrations)
- Docker & Docker Compose

## 🛠️ Configuração

### 1. Docker Compose

Execute o ambiente completo com Docker Compose:

```bash
docker-compose up -d
```

### 2. Executar a Aplicação

```bash
./mvnw spring-boot:run
# ou
./gradlew bootRun
```

A aplicação estará disponível em `http://localhost:8080`

## 🔐 Autenticação

A API utiliza JWT (JSON Web Tokens) para autenticação.

### Endpoints de Autenticação

#### Registrar Novo Usuário

**POST** `/auth/register`

```json
{
  "email": "usuario@exemplo.com",
  "password": "senha123"
}
```

**Resposta:**
- `200 OK` - Usuário criado com sucesso
- `400 Bad Request` - Email já existe ou dados inválidos

#### Login

**POST** `/auth/login`

```json
{
  "email": "usuario@exemplo.com",
  "password": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Usando o Token JWT

Para acessar endpoints protegidos, inclua o token no header Authorization:
```
"Authorization: Bearer SEU_TOKEN_AQUI"
```

## 🚨 Importante

- O token JWT expira em 1 hora
- Após a expiração, é necessário fazer login novamente
- Emails devem ser únicos no sistema
- Senhas são validadas e criptografadas automaticamente