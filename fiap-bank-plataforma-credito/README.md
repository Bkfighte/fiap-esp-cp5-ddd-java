# FIAP Bank - Plataforma de Crédito 💳

Microsserviço de **Esteira e Motor de Decisão de Crédito** do FIAP Bank. Responsável por processar propostas de crédito, avaliar regras de negócio e risco, comunicar-se com o Core Bancário via **Spring Cloud OpenFeign**, simular verificações no SCR (Banco Central) e Bureau de Crédito (Serasa/SPC), e notificar clientes.

Refatorado em arquitetura **DDD Multi-Módulo** (`domain`, `infrastructure`, `application` e `presentation`), expondo endpoints RESTful em `/api/v1/solicitacoes` e mantendo o controller `/api/credito` como adapter para a Mesa de Operações e o app Mobile.

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.1.1**
- **Spring Cloud OpenFeign 2025.1.0** (comunicação declarativa com o Core Bancário)
- **Spring Data JPA & Bean Validation**
- **Lombok**
- **Spring Boot Actuator**
- **H2 Database** (em memória)
- **Maven**

---

## 🚀 Como Executar a Aplicação

### 1. Compilar os módulos

Na raiz do diretório `fiap-bank-plataforma-credito`:

```bash
mvn clean install
```

### 2. Iniciar a aplicação via Maven

```bash
mvn -pl presentation spring-boot:run
```

A aplicação estará acessível em: **`http://localhost:8081`**

---

## 🗄️ Console do Banco de Dados H2

- **URL:** `http://localhost:8081/h2-console`
- **JDBC URL:** `jdbc:h2:mem:plataformacreditodb`
- **User Name:** `sa`
- **Password:** *(deixe em branco)*

---

## 🌐 Endpoints RESTful (`/api/v1/solicitacoes`)

- `GET /api/v1/solicitacoes` - Lista todas as solicitações de crédito (`200 OK`)
- `GET /api/v1/solicitacoes/{id}` - Busca uma solicitação por ID (`200 OK` / `404 Not Found`)
- `GET /api/v1/solicitacoes/cpf/{cpf}` - Lista solicitações de um cliente pelo CPF (`200 OK` / `400 Bad Request`)
- `GET /api/v1/solicitacoes/conta-core/{cpf}` - Consulta dados da conta no Core Bancário (`200 OK`)
- `POST /api/v1/solicitacoes` - Cria uma nova solicitação de crédito (`201 Created` / `400 Bad Request`)
- `PUT /api/v1/solicitacoes/{id}/aprovacao` - Aprova proposta na Mesa de Operações (`200 OK` / `404 Not Found` / `409 Conflict`)
- `PUT /api/v1/solicitacoes/{id}/reprovacao` - Reprova proposta na Mesa de Operações (`200 OK` / `400 Bad Request` / `404 Not Found` / `409 Conflict`)
- `DELETE /api/v1/solicitacoes/{id}` - Remove uma solicitação (`204 No Content` / `404 Not Found`)

---

## Endpoints Legados (`/api/credito`)

Mantidos para integração com a Mesa de Operações (`8082`) e o Internet Banking Mobile (`4200`):

- `POST /api/credito/solicitar`
- `POST /api/credito/consultar-status`
- `POST /api/credito/listar-todas`
- `POST /api/credito/buscar-detalhe`
- `POST /api/credito/aprovar-operacao`
- `POST /api/credito/reprovar-operacao`
- `POST /api/credito/consultar-conta-core`
