# FIAP Bank - Esteira de Crédito 🏦💳

Monorepo contendo o ecossistema completo da **Esteira de Crédito** do FIAP Bank, composto por quatro aplicações integradas: Core Bancário, Plataforma de Crédito, Mesa de Operações e Internet Banking Mobile.

---

## Aluno

- **Nome:** Bruno Koeke
- **RM:** 561309
- **Turma:** 2ESPH - Engenharia de Software

---

## 📁 Estrutura do Workspace e Documentação

Cada projeto possui seu próprio arquivo `README.md` detalhando tecnologias, arquitetura, rotas e instruções de execução:

| Projeto | Tecnologia | Porta | Descrição & Documentação |
| :--- | :--- | :---: | :--- |
| **`fiap-bank-core-bancario`** | Java 21, Spring Boot, JPA, DDD Multi-Módulo | `8080` | [README do Core Bancário](./fiap-bank-core-bancario/README.md) |
| **`fiap-bank-plataforma-credito`** | Java 21, Spring Boot, Spring Cloud Feign, DDD Multi-Módulo | `8081` | [README da Plataforma de Crédito](./fiap-bank-plataforma-credito/README.md) |
| **`fiap-bank-mesa-operacoes-credito`** | Java 21, Spring Boot, Thymeleaf, Feign | `8082` | [README da Mesa de Operações](./fiap-bank-mesa-operacoes-credito/README.md) |
| **`fiap-bank-internet-banking-mobile`** | Angular 17+, TypeScript, CSS Responsivo | `4200` | [README do Mobile Angular](./fiap-bank-internet-banking-mobile/README.md) |

---

## Alterações Realizadas no Checkpoint 5

- **`fiap-bank-core-bancario` (Fase 1):** Construído do zero na porta `8080` com estrutura Maven Multi-Módulo (`domain`, `infrastructure`, `application` e `presentation`). Na camada `domain` ficaram as entidades `Cliente` e `ContaBancaria`, os Value Objects (`Cpf`, `Email`, `Saldo`) e as interfaces de repositório sem dependência do Spring. A persistência em H2 (`jdbc:h2:mem:corebancariodb`) foi feita na camada `infrastructure` usando adapters, os casos de uso e DTOs com Bean Validation ficaram em `application`, e os controllers RESTful (`/api/v1/clientes` e `/api/v1/contas`) junto com o `GlobalExceptionHandler` ficaram em `presentation`.
- **`fiap-bank-plataforma-credito` (Fase 2):** Refatorado para a mesma estrutura de 4 módulos Maven (`domain`, `infrastructure`, `application` e `presentation`). A classe `SolicitacaoCredito` foi limpa das anotações do JPA e isolada no módulo `domain`, enquanto a entidade JPA e o `CoreBancarioFeignClient` foram movidos para `infrastructure`. Na camada `presentation`, foi criado o controller RESTful em `/api/v1/solicitacoes` e mantido o controller `/api/credito` como adapter para não quebrar a integração com a Mesa de Operações (`8082`) e o app mobile Angular (`4200`).

---

## 🚦 Ordem Recomendada de Execução

Para testar o fluxo de ponta a ponta com todas as integrações ativas, inicie os serviços na seguinte ordem:

### 1. Iniciar o Core Bancário (`Porta 8080`)
Responsável pela gestão de clientes e contas bancárias.
```bash
cd fiap-bank-core-bancario
mvn clean install
mvn -pl presentation spring-boot:run
```
- API Base: `http://localhost:8080/api/v1`
- H2 Console: `http://localhost:8080/h2-console`

---

### 2. Iniciar a Plataforma de Crédito (`Porta 8081`)
Motor de decisão e esteira, integrado via Spring Cloud OpenFeign ao Core Bancário.
```bash
cd fiap-bank-plataforma-credito
mvn clean install
mvn -pl presentation spring-boot:run
```
- API RESTful: `http://localhost:8081/api/v1/solicitacoes`
- API Adapter (Legado): `http://localhost:8081/api/credito`
- H2 Console: `http://localhost:8081/h2-console`

---

### 3. Iniciar a Mesa de Operações (`Porta 8082`)
Front-end Web Thymeleaf para analistas de risco aprovarem ou reprovarem propostas.
```bash
cd fiap-bank-mesa-operacoes-credito
mvn clean compile
mvn spring-boot:run
```
- Painel Web do Operador: `http://localhost:8082`

---

### 4. Iniciar o Internet Banking Mobile (`Porta 4200`)
Front-end Angular responsivo voltado ao cliente final para simulação e submissão de crédito.
```bash
cd fiap-bank-internet-banking-mobile
npm install
npm run start
```
- App Mobile Web: `http://localhost:4200`

---

## 🧪 Dados Pré-Carregados para Testes

Os serviços iniciam automaticamente com dados pré-populados para testes:

- **Cliente 1 (Aprovável):** CPF `12345678900` | Nome: Ana Silva | Renda: R$ 7.500,00 | Saldo: R$ 12.500,00
- **Cliente 2 (Aprovável):** CPF `98765432100` | Nome: Carlos Eduardo Oliveira | Renda: R$ 12.000,00 | Saldo: R$ 45.800,50
- **Cliente 3 (Reprovado SCR):** CPF `55544433399` | Nome: Mariana Souza | Restrição ativa no SCR
