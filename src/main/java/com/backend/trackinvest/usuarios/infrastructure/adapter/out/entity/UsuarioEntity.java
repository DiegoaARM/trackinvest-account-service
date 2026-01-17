package com.backend.trackinvest.usuarios.infrastructure.adapter.out.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 25)
    private String primerNombre;

    @Column(nullable = true, length = 25)
    private String segundoNombre;

    @Column(nullable = false, length = 25)
    private String PrimerApellido;

    @Column(nullable = true, length = 25)
    private String SegundoApellido;

    // Aquí "aplanamos" el Value Object Email
    @Column(nullable = false, unique = true)
    private String email;

    // Guardamos el hash de la contraseña (String)
    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
