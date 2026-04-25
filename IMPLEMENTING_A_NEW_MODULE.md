# Implementing a New Module in TrackInvest (English)

This document shows a step-by-step guide and code examples to implement a new module in the TrackInvest Account Service. The example module is named "Invoice". Follow naming and packaging conventions used in the repository (com.trackinvest.account...).

Goals (what you will create):
- Domain model (Domain class + enums)
- JPA Entity
- DTOs (request / response)
- Domain exceptions
- Domain rules (validators)
- MapStruct mappers
- Secondary port (RepositoryPort)
- Spring Data repository
- JPA persistence adapter (secondary adapter)
- Use cases (ports in + implementation)
- Controller adapter (primary adapter)
- Security configuration snippet

## 1. Domain model

They are located in the `domain/models`. They represent business concepts and contain business logic. 
We try to make the code as pure Java as possible, without lombok or other libraries.

In the domain layer, there should be no public constructors. 
Instead, there should be static methods that allow us to create the object instance. 
These methods can be `create` and `from`, and they must contain the corresponding validations. 

Furthermore, setters should not be added unless absolutely necessary. 
Instead, we will create functions that allow us to make a change to the object while simultaneously validating it, such as `changeFullName`.

````
public class UserDomain {

    private final UUID id;
    private final String cognitoId;
    private String fullname;
    private final String email;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<WalletDomain> walletsList;
    
    private UserDomain(UUID id, String cognitoId, String fullname, String email, LocalDateTime createdAt, LocalDateTime updatedAt, List<WalletDomain> walletsList) {
        this.id = Objects.requireNonNull(id, "ID is mandatory");
        this.cognitoId = Objects.requireNonNull(cognitoId, "Cognito ID is mandatory");
        this.fullname = Objects.requireNonNull(fullname, "fullname is mandatory");
        this.email = Objects.requireNonNull(email, "Email is mandatory");
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.walletsList = Objects.requireNonNull(walletsList, "wallets is mandatory");
    }
    
    public static UserDomain create(UUID id, String cognitoId, String fullname, String email, List<WalletDomain> walletsList) {
        LocalDateTime now = LocalDateTime.now();
        return new UserDomain(id, cognitoId, fullname, email, now, now, walletsList);
    }

    public static UserDomain create(UUID id, String cognitoId, String fullname, String email) {
        LocalDateTime now = LocalDateTime.now();
        return new UserDomain(id, cognitoId, fullname, email, now, now, new ArrayList<>());
    }
    
    public static UserDomain from(UUID id, String cognitoId, String fullname, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDomain(id, cognitoId, fullname, email, createdAt, updatedAt, new ArrayList<>());
    }
    
    public void changeFullname(String newfullname) {
        this.fullname = Objects.requireNonNull(newfullname, "New fullname cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public void addWallet(WalletDomain newWallet) {
        this.walletsList.add(newWallet);
    }
    
    //getters ...
````

## 2. JPA Entity

They are located in the `infrastructure/adapter/out/persistence/entity`.

They represent the database tables and are used by repositories to interact with the database.
Here we can use some libraries like Lombok.

```
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    private UUID id;

    @Column(name = "cognito_id",nullable = false, unique = true)
    private String cognitoId;

    @Column(nullable = false, length = 25)
    private String fullname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<WalletEntity> walletsList = new ArrayList<>();
}
```


## 3. DTOs

They are located in the `application/ports/in/dto`.

They are records used to transfer data between the infrastructure layer and the application layer.

DTOs must have data validation to comply with null-prescription practices. 
This involves implementing constructors and setters with data validation.

```
public record GetUserResponseDTO(
        UUID id,
        String cognitoId,
        String fullName,
        String email) {
}
```
5) Domain exceptions

Create meaningful domain exceptions extending the project's DomainException.

File: com.trackinvest.account.invoice.domain.exception.InvoiceNotFoundException.java

package com.trackinvest.account.invoice.domain.exception;

import com.trackinvest.account.common.domain.exception.DomainException;

public class InvoiceNotFoundException extends DomainException {
    private static final long serialVersionUID = 1L;

    private InvoiceNotFoundException(String message) { super(message); }

    public static InvoiceNotFoundException create() {
        return new InvoiceNotFoundException("Invoice not found");
    }
}

Also create InvalidInvoiceAmountException.java if needed.

6) Domain rules (validators)

Simple rule without repository access can live in domain.rules.

File: com.trackinvest.account.invoice.domain.rules.ValidateInvoiceAmountRule.java

package com.trackinvest.account.invoice.domain.rules;

import java.math.BigDecimal;

public class ValidateInvoiceAmountRule {
    public void validate(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invoice amount must be greater than zero");
        }
    }
}

If a rule needs DB access, implement it in application.rules with @Service and inject the repository port.

7) MapStruct mappers

Create two mappers: Entity <-> Domain and DTO <-> Domain.

File: com.trackinvest.account.invoice.application.mapper.InvoiceEntityMapper.java

package com.trackinvest.account.invoice.application.mapper;

import org.mapstruct.Mapper;
import com.trackinvest.account.invoice.infrastructure.persistence.InvoiceEntity;
import com.trackinvest.account.invoice.domain.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceEntityMapper {
    Invoice toDomain(InvoiceEntity entity);
    InvoiceEntity toEntity(Invoice domain);
}

File: com.trackinvest.account.invoice.application.mapper.InvoiceDTOMapper.java

package com.trackinvest.account.invoice.application.mapper;

import org.mapstruct.Mapper;
import com.trackinvest.account.invoice.application.dto.request.CreateInvoiceDTO;
import com.trackinvest.account.invoice.application.dto.response.InvoiceDTO;
import com.trackinvest.account.invoice.domain.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceDTOMapper {
    Invoice toDomain(CreateInvoiceDTO dto);
    InvoiceDTO toDTO(Invoice domain);
}

8) Ports out (repository port)

File: com.trackinvest.account.invoice.application.port.out.InvoiceRepositoryPort.java

package com.trackinvest.account.invoice.application.port.out;

import com.trackinvest.account.invoice.domain.Invoice;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepositoryPort {
    Invoice save(Invoice invoice);
    Optional<Invoice> findByIdAndSubscriptionId(UUID id, UUID subscriptionId);
}

9) Spring Data Repository (secondary adapter interface)

File: com.trackinvest.account.invoice.infrastructure.persistence.SpringInvoiceRepository.java

package com.trackinvest.account.invoice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SpringInvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {
    Optional<InvoiceEntity> findByIdAndSubscriptionId(UUID id, UUID subscriptionId);
}

10) JPA persistence adapter (secondary adapter)

File: com.trackinvest.account.invoice.infrastructure.persistence.InvoiceJpaAdapter.java

package com.trackinvest.account.invoice.infrastructure.persistence;

import org.springframework.stereotype.Component;
import com.trackinvest.account.invoice.application.port.out.InvoiceRepositoryPort;
import com.trackinvest.account.invoice.application.mapper.InvoiceEntityMapper;
import com.trackinvest.account.invoice.domain.Invoice;
import java.util.Optional;
import java.util.UUID;

@Component
public class InvoiceJpaAdapter implements InvoiceRepositoryPort {
    private final SpringInvoiceRepository repository;
    private final InvoiceEntityMapper mapper;

    public InvoiceJpaAdapter(SpringInvoiceRepository repository, InvoiceEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = mapper.toEntity(invoice);
        InvoiceEntity persisted = repository.save(entity);
        return mapper.toDomain(persisted);
    }

    @Override
    public Optional<Invoice> findByIdAndSubscriptionId(UUID id, UUID subscriptionId) {
        return repository.findByIdAndSubscriptionId(id, subscriptionId).map(mapper::toDomain);
    }
}

11) Use case port (in) and implementation

Port in:

File: com.trackinvest.account.invoice.application.port.in.CreateInvoiceUseCase.java

package com.trackinvest.account.invoice.application.port.in;

import com.trackinvest.account.invoice.application.dto.request.CreateInvoiceDTO;
import java.util.UUID;

public interface CreateInvoiceUseCase {
    void create(CreateInvoiceDTO dto, UUID subscriptionId);
}

Implementation:

File: com.trackinvest.account.invoice.application.usecase.CreateInvoiceUseCaseImpl.java

package com.trackinvest.account.invoice.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trackinvest.account.invoice.application.port.in.CreateInvoiceUseCase;
import com.trackinvest.account.invoice.application.port.out.InvoiceRepositoryPort;
import com.trackinvest.account.invoice.application.mapper.InvoiceDTOMapper;
import com.trackinvest.account.invoice.domain.rules.ValidateInvoiceAmountRule;
import com.trackinvest.account.invoice.domain.Invoice;
import java.util.UUID;

@Service
@Transactional
public class CreateInvoiceUseCaseImpl implements CreateInvoiceUseCase {
    private final InvoiceRepositoryPort repository;
    private final InvoiceDTOMapper dtoMapper;
    private final ValidateInvoiceAmountRule amountRule;

    public CreateInvoiceUseCaseImpl(InvoiceRepositoryPort repository,
                                    InvoiceDTOMapper dtoMapper,
                                    ValidateInvoiceAmountRule amountRule) {
        this.repository = repository;
        this.dtoMapper = dtoMapper;
        this.amountRule = amountRule;
    }

    @Override
    public void create(CreateInvoiceDTO dto, UUID subscriptionId) {
        amountRule.validate(dto.getAmount());
        Invoice invoice = dtoMapper.toDomain(dto);
        invoice.setSubscriptionId(subscriptionId);
        repository.save(invoice);
    }
}

12) Controller adapter (primary adapter)

File: com.trackinvest.account.invoice.infrastructure.controller.InvoiceController.java

package com.trackinvest.account.invoice.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import com.trackinvest.account.invoice.application.port.in.CreateInvoiceUseCase;
import com.trackinvest.account.invoice.application.dto.request.CreateInvoiceDTO;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final CreateInvoiceUseCase createInvoiceUseCase;

    public InvoiceController(CreateInvoiceUseCase createInvoiceUseCase) {
        this.createInvoiceUseCase = createInvoiceUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateInvoiceDTO dto,
                                       @RequestHeader(value = "x-subscription-id", required = false) UUID subscriptionId) {
        createInvoiceUseCase.create(dto, subscriptionId);
        return ResponseEntity.status(201).build();
    }
}

If your project uses interactors layer, implement an interactor to map DTOs and call use case. Otherwise controllers may call use cases directly.

13) Authorization and configuration

If TrackInvest uses Spring Security and JWT, protect endpoints and add required scopes/authorities.

Example (HttpSecurity DSL):

http
  .authorizeHttpRequests(auth -> auth
      .requestMatchers("/invoices/**").hasAuthority("SCOPE_invoices:write")
      .anyRequest().authenticated()
  );

Or use method security:

@PreAuthorize("hasAuthority('SCOPE_invoices:write')")
@PostMapping
public ResponseEntity<Void> create(...) { ... }

14) Tests

- Unit tests for domain rules and use case logic.
- Repository tests with @DataJpaTest for SpringInvoiceRepository.
- Integration tests for controller + full stack with @SpringBootTest.

15) Checklist (quick)
- [ ] Domain class + enums
- [ ] Domain exceptions
- [ ] Domain rules
- [ ] DTOs
- [ ] MapStruct mappers
- [ ] Ports (in/out)
- [ ] Use case implementations
- [ ] JPA Entity + Spring Data repository
- [ ] JPA Adapter implementing repository port
- [ ] Controller adapter
- [ ] Security configuration
- [ ] Tests

If you want, I can scaffold the Java files inside the repository according to these examples. Tell me which classes you want generated first and I will create them.

