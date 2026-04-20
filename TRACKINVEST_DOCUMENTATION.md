# TrackInvest — Technical Documentation

## Index
1. Description
2. System Architecture
3. Project Structure
4. Patterns & Principles
5. Domain Layer
6. Application Layer
7. Infrastructure Layer
8. Data Flow
9. Functional Modules
10. Business Logic Highlights
11. Accounts & Wallets System
12. Account Reconciliation (Cruce)
13. Reversals (Anulaciones)
14. Guideline for New Implementations
15. API Endpoints & Swagger
16. Code Conventions

---

## 1. Description
TrackInvest Account Service is a microservice responsible for managing users, wallets and account-related operations for the TrackInvest platform. It provides domain logic for accounts, multi-tenant enforcement and adapters to persist and expose functionality through REST.

Goals:
- Manage users and wallet balances
- Provide account and payment operations (register, query, reverse)
- Enforce multi-tenant isolation via suscripcionId (tenant id)
- Offer clear ports & adapters boundaries for maintainability and testability

Technology stack (as present in the repo):
- Java 21, Spring Boot 3.x
- JPA / Hibernate (PostgreSQL expected)
- MapStruct for mapping
- Maven build

## 2. System Architecture
The service follows a Hexagonal / Ports & Adapters architecture:
- Domain: Pure business logic, rules, exceptions
- Application: Use-cases, rules validators, ports (interfaces)
- Infrastructure: REST controllers, JPA repositories, adapters
- Cross-cutting: Authorization helpers, generic exceptions

High-level flow:
Controller (REST) → Interactor / DTO mappers → Use Case → Rules Validator → Repository (JPA) → Entity mapping → Persistence

## 3. Project Structure
Relevant root folders (based on repository):
- src/main/java/com/trackinvest/account/
  - TrackInvestAccountServiceApplication.java (application entry)
  - common/ (shared domain constructs, exceptions, services)
  - user/ (user module: application, domain, infrastructure)
  - wallet/ (wallet module: application, domain, infrastructure)
- src/main/resources/application.properties
- test/ (unit / integration tests)

Example packages and responsibilities:
- .common.domain: Domain exceptions, base domain types, domain rules
- .user.application.usecase: Use cases and rules validators for user flows
- .wallet.application.usecase: Use cases for wallet operations (credit, debit, sync)
- .infrastructure.adapter: Controllers and secondary adapters

## 4. Patterns & Principles
Applied principles:
- SOLID (SRP, OCP, DIP, ISP, LSP)
- Hexagonal architecture (Ports & Adapters)
- Repository pattern for data access
- Strategy pattern for domain rules
- Mapper pattern (MapStruct) for DTO ↔ Domain ↔ Entity

Design patterns used in codebase:
- Factory methods on DTOs and Domain objects
- Template method for rule validation flows
- Specification pattern for complex repository queries (if needed)

## 5. Domain Layer
- Contains domain models for User and Wallet, domain-specific exceptions and validation rules.
- Domain rules represented as DomainRule<T> implementations; throw domain exceptions on violations.
- Entities in domain are POJOs without infrastructure annotations; persistence lives in the infrastructure layer.

## 6. Application Layer
- Use Cases implement business flows (e.g., RegisterUser, CreditWallet, DebitWallet, GetWalletByUser).
- Each use case depends on repository ports and rules validators.
- Rules validators orchestrate individual domain rules which may be pure or repository-backed.
- Interactors transform DTOs into domain models and delegate to use cases.

Example use-case responsibilities:
- Validate tenant (suscripcionId) exists and ownership constraints
- Validate business rules (wallet balances, user states)
- Map domain to entity and persist

## 7. Infrastructure Layer
- Controllers in infrastructure expose REST endpoints; they accept DTOs and x-suscripcion-id header where appropriate.
- JPA Entities live in the infrastructure layer; repositories expose methods such as findByIdAndSuscripcion_Id and paging queries by suscripcion.
- Adapters map entities ↔ domain models using MapStruct mappers.

## 8. Data Flow
Create operation (POST):
1. Controller receives DTO and reads x-suscripcion-id header
2. Interactor maps DTO → Domain
3. Use case executes: rules validation → entity mapping → repository.save()
4. Controller returns GenericResponse

Query operation (GET):
1. Controller handles paging/sort params
2. Interactor triggers use case to build Specification and call repository
3. Entities → Domain → DTO responses, wrapped in a PageResponse

## 9. Functional Modules
- Users: CRUD, types (client, provider, both), validations
- Wallets: Credits, debits, sync, balance queries
- Accounts (Carteras): Aggregated account state per user
- Payments: Records of credits/debits, reversal support
- Auth (external or upstream): JWT / Authorization service used by common.auth

Note: This repository contains the account-focused subset (user and wallet). Other business modules (purchases, sales) are outside this service.

## 10. Business Logic Highlights
- Multi-tenant enforcement: All persistent objects are associated to a suscripcionId; repository queries must filter by suscripcion
- Wallet operations must validate resulting balance (no negative unless allowed by rule)
- Reversals must validate original operation existence and statuses
- Rules are single-responsibility classes and should be reused by validators

## 11. Accounts & Wallets System
Key concepts:
- Wallet Domain holds available balance and ledger of operations
- Operations are modeled as domain events / entities (e.g., WalletTransaction)
- Use cases: RegisterCredit, RegisterDebit, ReverseTransaction, GetWalletByUser

Persistence considerations:
- WalletEntity has relation to UserEntity and SuscripcionEntity
- Use optimistic locking or versioning if concurrent updates are expected

## 12. Account Reconciliation (Cruce)
- Reconciliation between accounts (clients/proveedores) should be implemented as a use-case that:
  1) receives two user identifiers and amounts
  2) validates balances and permissions
  3) creates offsetting transactions in both wallets
  4) persist results atomically (transactional)

## 13. Reversals (Anulaciones)
- Reversal use-case must:
  - Verify original transaction exists and belongs to the same suscripcion
  - Check current status allows reversal
  - Create compensating transaction and mark original as reversed
  - Emit domain events or notifications if needed

## 14. Guideline for New Implementations
When implementing a new feature:
1. Add domain POJOs and domain rules under domain/..
2. Create use case interfaces under application/usecase and implementations under impl/
3. Create rules validators aggregating domain rules
4. Add DTOs and interactor implementations under application/primaryports
5. Create JPA entities, repository interfaces and MapStruct mappers under infrastructure
6. Add REST controller under infrastructure.primaryadapters.controller
7. Add unit tests for rules and use cases, integration tests for controllers

Keep logic in domain/application layers; avoid business logic inside controllers or repositories.

## 15. API Endpoints & Swagger
- The project includes API documentation references (see DocumentacionSwagger.txt in repo root).
- Controllers expect an optional header: x-suscripcion-id (UUID) to enforce tenant scoping.
- Follow existing response wrapping (GenericResponse / PageResponse) conventions.

Suggested endpoints (examples based on modules present):
- POST /users — register user (header: x-suscripcion-id)
- GET /users — list users (paged)
- GET /users/{id} — user detail
- POST /wallets/{userId}/credit — credit wallet
- POST /wallets/{userId}/debit — debit wallet
- POST /wallets/transactions/{id}/reverse — reverse transaction

Refer to existing controllers and DocumentacionSwagger.txt for exact contracts.

## 16. Code Conventions
- Use factory static create() methods on DTOs, Entities and Domain where appropriate
- Default field values via helpers (TextHelper, UUIDHelper) where available
- Keep exceptions as typed domain exceptions (DomainException, RequiredAttributeException)
- Validate tenant id from header in controllers and propagate into DTOs/interactors
- MapStruct mappers used as singletons (INSTANCE pattern) or Spring-managed when injection required

---

If you want, I can: generate a summarized README, add a swagger example file, or create a template module (use-case + controller + tests) following these guidelines. Let me know which you prefer.
