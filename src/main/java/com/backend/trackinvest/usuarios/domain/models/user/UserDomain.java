package com.backend.trackinvest.usuarios.domain.models.user;

import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Name;
import com.backend.trackinvest.usuarios.domain.models.wallet.WalletDomain;
import com.backend.trackinvest.usuarios.domain.models.wallet.valueobjects.CurrencyTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class UserDomain {

    private final UUID id;
    private final String cognitoId;
    private Name name;
    private final Email email;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<WalletDomain> wallets;

    private UserDomain(UUID id, String cognitoId, Name name, Email email, LocalDateTime createdAt, LocalDateTime updatedAt, List<WalletDomain> wallets) {
        this.id = Objects.requireNonNull(id, "ID is mandatory");
        this.cognitoId = Objects.requireNonNull(cognitoId, "Cognito ID is mandatory");
        this.name = Objects.requireNonNull(name, "Name is mandatory");
        this.email = Objects.requireNonNull(email, "Email is mandatory");
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.wallets = Objects.requireNonNull(wallets, "Wallets is mandatory");
    }

    public static UserDomain create(UUID id, String cognitoId, Name name, Email email, List<WalletDomain> wallets) {
        LocalDateTime now = LocalDateTime.now();
        return new UserDomain(id, cognitoId, name, email, now, now, wallets);
    }

    public static UserDomain create(UUID id, String cognitoId, Name name, Email email) {
        LocalDateTime now = LocalDateTime.now();
        return new UserDomain(id, cognitoId, name, email, now, now, new ArrayList<>());
    }

    public static UserDomain from(UUID id, String cognitoId, Name name, Email email, LocalDateTime createdAt, LocalDateTime updatedAt, List<WalletDomain> wallets) {
        return new UserDomain(id, cognitoId, name, email, createdAt, updatedAt, wallets);
    }

    public static UserDomain from(UUID id, String cognitoId, Name name, Email email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDomain(id, cognitoId, name, email, createdAt, updatedAt, new ArrayList<>());
    }

    public void changeName(Name newName) {
        this.name = Objects.requireNonNull(newName, "New name cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public void addWallet(WalletDomain newWallet) {
        this.wallets.add(newWallet);
    }

    public UUID getId() {
        return id;
    }
    public String getCognitoId() {
        return cognitoId;
    }
    public Name getName() {
        return name;
    }
    public Email getEmail() {
        return email;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public List<WalletDomain> getWallets() {
        return wallets;
    }
}
