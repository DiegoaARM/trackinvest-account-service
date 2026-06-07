# 📈 TrackInvest - Account Service

![Java CI](https://github.com/DiegoaARM/trackinvest-account-service/actions/workflows/ci.yml/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=DiegoaARM_trackinvest-account-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=DiegoaARM_trackinvest-account-service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=DiegoaARM_trackinvest-account-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=DiegoaARM_trackinvest-account-service)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=DiegoaARM_trackinvest-account-service&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=DiegoaARM_trackinvest-account-service)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=DiegoaARM_trackinvest-account-service&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=DiegoaARM_trackinvest-account-service)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=DiegoaARM_trackinvest-account-service&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=DiegoaARM_trackinvest-account-service)

**TrackInvest - Account Service** is a microservice for user identity management, account security, and investment wallet administration. It is built following **Hexagonal Architecture** (Ports & Adapters), **Clean Architecture**, and **Domain-Driven Design (DDD)** principles — ensuring all business rules are natively protected in the core domain, decoupled from any external framework.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Software Architecture](#software-architecture)
- [Key Features](#key-features)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing Strategy](#testing-strategy)
- [CI/CD & Quality Gates](#cicd--quality-gates)
- [Observability](#observability)
- [Related Documentation](#related-documentation)

---

## Tech Stack

The project ecosystem is automated and designed under high-availability, observability, and rigorous code quality standards.

### ☕ Core, Language & Architectural Pattern

**Java 21** — Leverages the latest stable language features (Records, Pattern Matching, strong typing) to keep the codebase modern, concise, and efficient.

**Spring Boot 4** — Used exclusively in the Infrastructure layer as a supporting framework for the application lifecycle, dependency injection, and REST controller exposure. It never leaks into the domain or application layers.

**Hexagonal Architecture** — Internally divided into three rigid zones: **Domain** (pure business logic), **Ports** (inbound/outbound interfaces), and **Adapters** (external technology bindings). This enforces total separation between business rules and frameworks.

### 📡 Communication & Interconnection (Microservices)

**RabbitMQ** — Message broker powering the Event-Driven Architecture (EDA). When a domain event occurs in Account Service (e.g., user creation or wallet update), it publishes asynchronous, decoupled notifications to the rest of the microservice ecosystem.

### 🔐 Security & Identity

**AWS Cognito** — Cloud-based Identity Provider (IdP). Handles user registration, authentication, and secure JWT token issuance, so the service never stores credentials directly.

### 💾 Database & Persistence

**PostgreSQL 18** — Next-generation relational database engine used to store business entities (Users, Wallets) in an isolated, autonomous manner. Accessed exclusively through outbound adapters.

### 🧪 Code Quality & Test Suite (Unit, Integration & E2E)

**JUnit 5 & Mockito** — Tools for unit testing focused on Domain and Use Case layers. Uses `@ParameterizedTest` to massively validate business rules with minimal code.

**JaCoCo (Java Code Coverage)** — Coverage engine that analyzes unit test reach, configured to ignore the infrastructure layer so it focuses 100% on actual business value.

**Integration Tests** — Tests focused on validating correct adapter communication with real components (Database, Message Broker).

**End-to-End (E2E) Tests with Selenium** — Automates complete user flows simulating real browser behavior from end to end (Frontend + API + DB + RabbitMQ).

### 🐳 Deployment, Orchestration & DevOps (AWS Cloud)

**Docker** — Containerizes every component to guarantee identical, reproducible execution environments from local development to production.

**Kubernetes (K8s) / Amazon EKS** — Orchestrator handling managed deployment, automatic dynamic scaling based on demand, and self-healing of containers in AWS.

### 📊 Observability & Monitoring

**Prometheus** — Real-time metrics collector that monitors microservice health (memory, CPU, request rate, RabbitMQ queue depth).

**Grafana** — Interactive visual dashboard used by the team to interpret Prometheus-collected metrics and configure system alerts.

### 🚀 Automation & Continuous Integration (CI/CD)

**GitHub Actions** — Automated pipeline (`ci.yml`) triggered on every push or Pull Request to main branches, running the full build and test suite inside Docker containers.

**SonarCloud / SonarQube** — Static code analysis platform integrated into the pipeline. Features a mandatory **Quality Gate** linked to GitHub branch protection rules (`build-and-analyze`). If the code fails to meet coverage thresholds or contains bugs/vulnerabilities, the pipeline fails and blocks the merge.

---

## Software Architecture

The project is structured into three rigid layers following the **Ports & Adapters** pattern. Each **bounded context** (module) — `user`, `wallet`, and `common` — mirrors this structure internally.

```
trackinvest-account-service
 └── src/
      ├── main/java/com/trackinvest/account/
      │    ├── common/                          # Shared cross-cutting concerns
      │    ├── {module}/                        # Each bounded context (user, wallet, ...)
      │    │    ├── domain/                     # 💠 THE CORE: Pure business logic, zero external deps
      │    │    │    ├── models/                #     Domain entities & value objects
      │    │    │    ├── rules/                 #     Domain validation rules
      │    │    │    ├── service/               #     Domain services
      │    │    │    └── exception/             #     Business & format exceptions
      │    │    │         ├── business/         #         (e.g. InsufficientBalanceException)
      │    │    │         └── format/           #         (e.g. InvalidNameException)
      │    │    ├── application/                # 🧩 USE CASES: Orchestrate business flows
      │    │    │    ├── ports/in/              #     Inbound port interfaces (DTOs & service contracts)
      │    │    │    ├── ports/out/             #     Outbound port interfaces (repository, provider)
      │    │    │    └── usecase/               #     Concrete use case implementations
      │    │    └── infrastructure/             # 🔌 ADAPTERS: Framework & technology bindings
      │    │         ├── adapter/in/            #     Inbound adapters (REST controllers)
      │    │         ├── adapter/out/           #     Outbound adapters (JPA, Cognito, JWT, BCrypt)
      │    │         ├── config/                #     Spring configuration
      │    │         └── handler/               #     Web-layer exception handlers
      │    └── resources/                       # Application properties, static resources
      └── test/java/com/trackinvest/account/    # Mirrors main structure
           ├── {module}/application/usecase/    # Use case unit tests
           └── {module}/domain/rules/           # Domain rule unit tests
```

### Layer Responsibilities

| Layer            | Description                                                                                             |
|------------------|---------------------------------------------------------------------------------------------------------|
| **`domain/`**    | The innermost ring. Contains domain models (`UserDomain`, `WalletDomain`), value objects, validation rules (`DomainRule` implementations), and domain exceptions. Has zero external dependencies — no Spring, no JPA, no HTTP. Every business invariant is enforced here. |
| **`application/`** | The use case layer. Defines **inbound ports** (interfaces for what the application does) and **outbound ports** (interfaces for what it needs from outside). Use cases orchestrate domain logic by talking only through these ports. Never depends on infrastructure directly. |
| **`infrastructure/`** | The outermost ring. Implements inbound adapters (REST controllers via Spring MVC) and outbound adapters (JPA persistence, AWS Cognito, JWT, BCrypt). This is the only layer with framework dependencies. |

### Module Breakdown (Bounded Contexts)

| Module       | Responsibility                                                                 |
|--------------|---------------------------------------------------------------------------------|
| **`common/`** | Shared artifacts: `ApiResponse` DTO, base `DomainRule` interface, global exceptions (`TrackinvestException`), security config (`JwtFilter`, `SecurityConfig`), `GlobalExceptionHandler`, and OpenAPI config. |
| **`user/`**   | User identity & authentication: sync users from Cognito, token-based auth (auth code flow, refresh token), user profile management (get me, change name), domain validation of user attributes. |
| **`wallet/`** | Investment wallet CRUD: create, update, delete wallets; balance updates with domain-enforced rules (no overdraft, max wallet count, duplicate name prevention, last-wallet protection). |

### Dependency Rule

> **Dependencies point inward.** `infrastructure` depends on `application` which depends on `domain`. `domain` depends on nothing.

```
infrastructure  →  application  →  domain  →  (pure Java)
     ↑                    ↑               ↑
  (Spring, JPA,      (Use cases,      (Entities,
   Cognito, HTTP)      Ports)          Rules)
```

---

## Key Features

- **Centralized Identity:** Automatic user synchronization from AWS Cognito to local PostgreSQL database.
- **Token-Based Authentication:** OAuth2 authorization code flow, refresh token rotation, and JWT validation.
- **Domain-Driven Validation:** Business rules enforced at the domain layer via composable `DomainRule` implementations (e.g., `UserNameValidRule`, `WalletAmountValidRule`).
- **Wallet Management:** Full CRUD with rich domain invariants — insufficient balance protection, duplicate name detection, maximum wallet limits, and last-wallet deletion safeguards.
- **Event-Driven Architecture:** RabbitMQ integration for publishing domain events (user created, wallet updated) to other microservices asynchronously.
- **API Documentation:** Interactive Swagger UI for endpoint exploration and testing.

---

## Getting Started

### Prerequisites

- Java 21 (LTS)
- Docker & Docker Compose
- PostgreSQL 18 (or use the provided Docker Compose setup)
- AWS Cognito User Pool (with configured app client)

### Local Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/DiegoaARM/trackinvest-account-service.git
   cd trackinvest-account-service
   ```

2. **Configure environment variables** (or update `application.properties`):

   ```properties
   # Database
   spring.datasource.url=jdbc:postgresql://localhost:5432/trackinvest
   spring.datasource.username=your_user
   spring.datasource.password=your_password

   # AWS Cognito
   cognito.client-id=your_client_id
   cognito.client-secret=your_client_secret
   cognito.user-pool-id=your_user_pool_id
   cognito.region=your_region

   # RabbitMQ
   spring.rabbitmq.host=localhost
   spring.rabbitmq.port=5672
   ```

3. **Run with Maven:**

   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access Swagger UI:**

   ```
   http://localhost:8080/swagger-ui/index.html
   ```

---

## API Documentation

Interactive API documentation is available via **Swagger UI** (OpenAPI 3) when running the service:

```
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI specification is auto-generated and exposes all REST endpoints for the `user` and `wallet` modules. Detailed example requests and responses can be found in:

- [EJEMPLOS_API.md](./EJEMPLOS_API.md)
- [DocumentacionSwagger.md](./DocumentacionSwagger.md)

---

## Testing Strategy

| Type                | Tools                    | Scope                                                       |
|---------------------|--------------------------|-------------------------------------------------------------|
| **Unit Tests**      | JUnit 5, Mockito         | Domain rules (parametrized tests) & use case orchestration  |
| **Integration Tests** | Spring Boot Test       | Adapter communication with real DB / message broker         |
| **E2E Tests**       | Selenium                 | Full user flows (Frontend + API + DB + RabbitMQ)            |

- **JaCoCo** is configured to enforce coverage on `domain` and `application` layers only (infrastructure is excluded from coverage targets).
- Run tests with:
  ```bash
  ./mvnw verify
  ```

---

## CI/CD & Quality Gates

The project uses **GitHub Actions** (`.github/workflows/ci.yml`) triggered on every push and pull request to main branches:

1. **Build & Test** — Compile the project and run the full test suite inside Docker containers.
2. **Static Analysis** — SonarCloud scans the code for bugs, vulnerabilities, code smells, and technical debt.
3. **Quality Gate** — A **mandatory Quality Gate** is enforced via branch protection rules. The pipeline fails (blocking merge) if coverage drops below the threshold or if any blocker/critical issues are found.

---

## Observability

- **Prometheus** collects real-time metrics: memory, CPU, request rate, and RabbitMQ queue depth.
- **Grafana** provides visual dashboards for the team to monitor health and set up alerts.

Metrics endpoints are exposed at:

```
http://localhost:8080/actuator/prometheus
```

---

## Related Documentation

| Document                                                   | Description                                |
|------------------------------------------------------------|--------------------------------------------|
| [IMPLEMENTING_A_NEW_MODULE.md](./IMPLEMENTING_A_NEW_MODULE.md) | Guide for adding a new bounded context     |
| [IMPLEMENTACION_USUARIOS_BILLETERAS.md](./IMPLEMENTACION_USUARIOS_BILLETERAS.md) | User & wallet implementation details       |
| [DocumentacionCognito.md](./DocumentacionCognito.md)       | AWS Cognito integration reference          |
| [DocumentacionSwagger.md](./DocumentacionSwagger.md)       | Swagger/OpenAPI documentation              |
| [EJEMPLOS_API.md](./EJEMPLOS_API.md)                       | API request/response examples              |
| [TRACKINVEST_DOCUMENTATION.md](./TRACKINVEST_DOCUMENTATION.md) | General project documentation              |

---

## License

Distributed under the MIT License. See [LICENSE](./LICENSE) for more information.
