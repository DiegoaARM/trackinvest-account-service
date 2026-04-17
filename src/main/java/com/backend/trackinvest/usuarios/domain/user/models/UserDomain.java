package com.backend.trackinvest.usuarios.domain.user.models;

import com.backend.trackinvest.usuarios.domain.wallet.models.WalletDomain;

import java.time.LocalDateTime;
import java.util.*;

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

    public static UserDomain from(UUID id, String cognitoId, String fullname, String email, LocalDateTime createdAt, LocalDateTime updatedAt, List<WalletDomain> walletsList) {
        return new UserDomain(id, cognitoId, fullname, email, createdAt, updatedAt, walletsList);
    }

    public static UserDomain from(UUID id, String cognitoId, String fullname, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDomain(id, cognitoId, fullname, email, createdAt, updatedAt, new ArrayList<>());
    }

    public void changefullname(String newfullname) {
        this.fullname = Objects.requireNonNull(newfullname, "New fullname cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public void addWallet(WalletDomain newWallet) {
        this.walletsList.add(newWallet);
    }

    public UUID getId() {
        return id;
    }
    public String getCognitoId() {
        return cognitoId;
    }
    public String getFullname() {
        return fullname;
    }
    public String getEmail() {
        return email;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public List<WalletDomain> getWalletsList() {
        return walletsList;
    }
}
