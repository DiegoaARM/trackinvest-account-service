package com.backend.trackinvest.usuarios.application.ports.out;

import com.backend.trackinvest.usuarios.domain.models.UsuarioDomain;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {

    Optional<UsuarioDomain> findById(UUID id);

    Optional<UsuarioDomain> findByEmail(String email);

    UsuarioDomain save(UsuarioDomain usuario);

    boolean existsByEmail(String email);
}
