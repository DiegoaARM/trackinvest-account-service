package com.backend.trackinvest.usuarios.domain.models.user;

import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Name;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Password;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class UserDomain {

    private final UUID id;
    private final String cognito_id;
    private Name name;
    private final Email email;
    private Password password;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDomain(UUID id, String cognitoId, Name name, Email email, Password password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "ID is mandatory");
        cognito_id = cognitoId;
        this.name = Objects.requireNonNull(name, "Name is mandatory");
        this.email = Objects.requireNonNull(email, "Email is mandatory");
        this.password = Objects.requireNonNull(password, "Password is mandatory");
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static UserDomain create(UUID id, String cognito_id, Name name, Email email, Password password) {
        LocalDateTime ahora = LocalDateTime.now();
        return new UserDomain(id, cognito_id, name, email, password, ahora, ahora);
    }

    public static UserDomain from(UUID id, String cognito_id, Name name, Email email, Password password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDomain(id, cognito_id, name, email, password, createdAt, updatedAt);
    }

    public void changeName(Name newName) {
        this.name = Objects.requireNonNull(newName, "New name cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }
    public String getCognito_id() {
        return cognito_id;
    }
    public Name getName() {
        return name;
    }
    public Email getEmail() {
        return email;
    }
    public Password getPassword() {return password;}
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
