package com.trackinvest.account.user.domain.models;

import com.trackinvest.account.user.domain.rules.UserNameValidRule;
import com.trackinvest.account.user.domain.rules.UserValidatorRule;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import java.time.LocalDateTime;
import java.util.*;

public class UserDomain {

    private final UUID id;
    private final String cognitoId;
    private String fullname;
    private final String email;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final List<WalletDomain> walletsList;

    private UserDomain(UUID id, String cognitoId, String fullname, String email, LocalDateTime createdAt, LocalDateTime updatedAt, List<WalletDomain> walletsList) {
        this.id = Objects.requireNonNull(id, "ID is mandatory");
        this.cognitoId = Objects.requireNonNull(cognitoId, "Cognito ID is mandatory");
        this.fullname = Objects.requireNonNull(fullname, "fullname is mandatory");
        this.email = Objects.requireNonNull(email, "Email is mandatory");
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.walletsList = Objects.requireNonNull(walletsList, "wallets is mandatory");
    }

    private UserDomain(UUID id) {
        this.id = Objects.requireNonNull(id, "ID is mandatory");
        this.cognitoId = null;
        this.fullname = null;
        this.email = null;
        this.createdAt = null;
        this.updatedAt = null;
        this.walletsList = new ArrayList<>();
    }

    public static UserDomain create(UUID id, String cognitoId, String fullname, String email) {
        LocalDateTime now = LocalDateTime.now();
        UserDomain user = new UserDomain(id, cognitoId, fullname, email, now, now, new ArrayList<>());
        return new UserValidatorRule().validate(user);
    }

    public static UserDomain create(UUID id) {
        return new UserDomain(id);
    }

    public static UserDomain from(UUID id, String cognitoId, String fullname, String email, LocalDateTime createdAt, LocalDateTime updatedAt, List<WalletDomain> walletsList) {
        return new UserDomain(id, cognitoId, fullname, email, createdAt, updatedAt, walletsList);
    }

    public static UserDomain from(UUID id, String cognitoId, String fullname, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDomain(id, cognitoId, fullname, email, createdAt, updatedAt, new ArrayList<>());
    }

    public void changeFullname(String newfullname) {
        UserNameValidRule.validate(newfullname);
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