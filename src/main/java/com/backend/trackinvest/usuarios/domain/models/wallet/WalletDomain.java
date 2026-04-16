package com.backend.trackinvest.usuarios.domain.models.wallet;

import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.domain.models.wallet.valueobjects.CurrencyTypeEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public class WalletDomain {

    private final UUID id;
    private String name;
    private final UserDomain user;
    private BigDecimal balance;
    private CurrencyTypeEnum currency;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private WalletDomain(UUID id, String name, UserDomain user, BigDecimal balance, CurrencyTypeEnum currency, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.balance = balance;
        this.currency = currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static WalletDomain create(UUID id, String name, UserDomain user, CurrencyTypeEnum currency) {
        LocalDateTime now = LocalDateTime.now();
        return new WalletDomain(id, name, user, BigDecimal.ZERO, currency, now, now);
    }

    public static WalletDomain createDefault(UUID id, String name, UserDomain user, CurrencyTypeEnum currency) {
        LocalDateTime now = LocalDateTime.now();
        return new WalletDomain(id, name, user, BigDecimal.ZERO, currency, now, now);
    }

    public static WalletDomain from(UUID id, String name, UserDomain user, BigDecimal balance, CurrencyTypeEnum currency, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new WalletDomain(id, name, user, balance, currency, createdAt, updatedAt);
    }

    public static WalletDomain from(UUID id, String name, BigDecimal balance, CurrencyTypeEnum currency, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new WalletDomain(id, name, null, balance, currency, createdAt, updatedAt);
    }

    public void changeName(String newName) {
        this.name = Objects.requireNonNull(newName, "New name cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal changeBalance(BigDecimal newBalance) {
        this.balance = Objects.requireNonNull(newBalance, "New balance cannot be null");
        this.updatedAt = LocalDateTime.now();
        return this.balance;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public UserDomain getUser() {
        return user;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public CurrencyTypeEnum getCurrency() {
        return currency;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
