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

Important: adapt package names and class names to your project conventions.

1) Packages and naming
- Base package: com.trackinvest.account.invoice
- Domain: com.trackinvest.account.invoice.domain
- DTOs: com.trackinvest.account.invoice.application.dto
- Ports in: com.trackinvest.account.invoice.application.port.in
- Ports out: com.trackinvest.account.invoice.application.port.out
- Usecases: com.trackinvest.account.invoice.application.usecase
- Mappers: com.trackinvest.account.invoice.application.mapper
- Persistence entity and adapters: com.trackinvest.account.invoice.infrastructure.persistence
- Controller: com.trackinvest.account.invoice.infrastructure.controller

2) Domain model

File: com.trackinvest.account.invoice.domain.Invoice.java

Example:

package com.trackinvest.account.invoice.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Invoice {
    private UUID id;
    private UUID subscriptionId;
    private UUID customerId;
    private BigDecimal amount;
    private InvoiceStatus status;

    public Invoice() { }

    public Invoice(UUID id, UUID subscriptionId, UUID customerId, BigDecimal amount, InvoiceStatus status) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
    }

    public static Invoice create(UUID id, UUID subscriptionId, UUID customerId, BigDecimal amount) {
        return new Invoice(id, subscriptionId, customerId, amount, InvoiceStatus.PENDING);
    }

    // Getters / setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(UUID subscriptionId) { this.subscriptionId = subscriptionId; }
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public InvoiceStatus getStatus() { return status; }
    public void setStatus(InvoiceStatus status) { this.status = status; }
}

File: com.trackinvest.account.invoice.domain.InvoiceStatus.java

public enum InvoiceStatus {
    PENDING, PAID, CANCELED
}

3) JPA Entity

File: com.trackinvest.account.invoice.infrastructure.persistence.InvoiceEntity.java

Example:

package com.trackinvest.account.invoice.infrastructure.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private com.trackinvest.account.invoice.domain.InvoiceStatus status;

    public static InvoiceEntity create(UUID id) {
        InvoiceEntity e = new InvoiceEntity();
        e.setId(id);
        return e;
    }

    // Getters / setters
}

4) DTOs

Create request and response DTOs under application.dto.request and application.dto.response.

File: com.trackinvest.account.invoice.application.dto.request.CreateInvoiceDTO.java

package com.trackinvest.account.invoice.application.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateInvoiceDTO {
    private UUID customerId;
    private BigDecimal amount;

    public CreateInvoiceDTO() { }
    public CreateInvoiceDTO(UUID customerId, BigDecimal amount) {
        this.customerId = customerId; this.amount = amount;
    }
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

File: com.trackinvest.account.invoice.application.dto.response.InvoiceDTO.java

package com.trackinvest.account.invoice.application.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class InvoiceDTO {
    private UUID id;
    private UUID customerId;
    private BigDecimal amount;
    private String status;

    public InvoiceDTO() { }
    public InvoiceDTO(UUID id, UUID customerId, BigDecimal amount, String status) {
        this.id = id; this.customerId = customerId; this.amount = amount; this.status = status;
    }
    // getters / setters
}

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

