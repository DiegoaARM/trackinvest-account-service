package com.backend.trackinvest.usuarios.domain.models;

import com.backend.trackinvest.usuarios.domain.exception.format.NombreInvalidoException;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Nombre;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Password;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class UsuarioDomain {

    private final UUID id;
    private Nombre nombre;
    private final Email email;
    private Password password;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UsuarioDomain(UUID id, Nombre nombre, Email email, Password password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "El ID es obligatorio");
        this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
        this.email = Objects.requireNonNull(email, "El email es obligatorio");
        this.password = Objects.requireNonNull(password, "El password es obligatorio");
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static UsuarioDomain create(UUID id, Nombre nombre, Email email, Password password) {
        LocalDateTime ahora = LocalDateTime.now();
        return new UsuarioDomain(id, nombre, email, password, ahora, ahora);
    }

    public static UsuarioDomain from(UUID id, Nombre nombre, Email email, Password password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UsuarioDomain(id, nombre, email, password, createdAt, updatedAt);
    }

    public void cambiarNombre(Nombre nuevoNombre) {
        this.nombre = Objects.requireNonNull(nuevoNombre, "El nuevo nombre no puede ser nulo");
        this.updatedAt = LocalDateTime.now();
    }

    public void cambiarContraseña(Password nuevaPassword) {
        this.password = Objects.requireNonNull(nuevaPassword, "La nueva contraseña no puede ser nula");
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }
    public Nombre getNombre() {
        return nombre;
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
