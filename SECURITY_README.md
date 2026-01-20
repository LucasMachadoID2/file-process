# Autenticação com Spring Security

Este projeto foi configurado com autenticação baseada em sessão usando Spring Security, sem JWT.

## Configurações Implementadas

### Dependências Adicionadas
- Spring Boot Starter Web
- Spring Boot Starter Security  
- Spring Boot Starter Data JPA
- H2 Database (para desenvolvimento)
- Spring Security Test

### Estrutura de Pacotes
```
br.com.fiap.file_process/
├── config/
│   ├── SecurityConfig.java     # Configuração do Spring Security
│   └── DataInitializer.java   # Dados iniciais
├── controller/
│   ├── AuthController.java     # Endpoints web de autenticação
│   ├── AdminController.java    # Administração de usuários
│   └── UserRestController.java # API REST
├── model/
│   └── User.java              # Entidade de usuário
├── repository/
│   └── UserRepository.java     # Repositório JPA
└── service/
    ├── CustomUserDetailsService.java # Implementação UserDetailsService
    └── UserService.java       # Serviço de gerenciamento de usuários
```

## Endpoints Disponíveis

### Web (Thymeleaf)
- `GET /` - Página inicial
- `GET /login` - Formulário de login
- `GET /register` - Formulário de cadastro
- `POST /register` - Cadastrar novo usuário
- `GET /dashboard` - Dashboard do usuário (autenticado)
- `GET /profile` - Perfil do usuário (autenticado)

### Administração
- `GET /admin/users` - Listar todos usuários (ADMIN)
- `GET /admin/users/edit/{id}` - Editar usuário (ADMIN)
- `POST /admin/users/edit/{id}` - Atualizar usuário (ADMIN)
- `POST /admin/users/delete/{id}` - Excluir usuário (ADMIN)

### API REST
- `POST /api/users/register` - Cadastrar usuário
- `GET /api/users/profile` - Obter perfil do usuário autenticado
- `GET /api/users/all` - Listar todos usuários (ADMIN)
- `GET /api/users/{id}` - Obter usuário por ID (ADMIN)
- `PUT /api/users/{id}` - Atualizar usuário (ADMIN ou próprio usuário)
- `DELETE /api/users/{id}` - Excluir usuário (ADMIN)

## Usuários Padrão

O sistema cria automaticamente dois usuários na inicialização:

### Administrador
- **Username:** admin
- **Password:** admin123
- **Role:** ADMIN

### Usuário Comum
- **Username:** user
- **Password:** user123
- **Role:** USER

## Configurações de Segurança

### Regras de Acesso
- Páginas públicas: `/`, `/home`, `/register`, `/css/**`, `/js/**`, `/images/**`
- Área de administrador: `/admin/**` (requer ROLE_ADMIN)
- Demais endpoints: requer autenticação

### Configurações de Sessão
- Timeout: 30 minutos
- Cookie HTTP-only: habilitado
- Máximo de sessões por usuário: 1

## Banco de Dados

### H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: sa
- Password: (vazio)

### Schema
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(255) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN DEFAULT TRUE
);
```

## Como Usar

### 1. Iniciar a Aplicação
```bash
./gradlew bootRun
```

### 2. Acessar a Aplicação
- URL: `http://localhost:8080`

### 3. Login
- Use os usuários padrão ou cadastre um novo usuário em `/register`

### 4. Testar API
```bash
# Cadastrar novo usuário via API
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123","email":"test@example.com"}'

# Acessar perfil (requer login via browser primeiro)
curl -X GET http://localhost:8080/api/users/profile \
  -H "Cookie: JSESSIONID=..."
```

## Próximos Passos

1. Implementar páginas HTML/Thymeleaf para os formulários
2. Adicionar validações nos formulários
3. Implementar recuperação de senha
4. Adicionar logging de auditoria
5. Configurar HTTPS para produção
6. Implementar CSRF protection customizado
7. Adicionar testes unitários e de integração
