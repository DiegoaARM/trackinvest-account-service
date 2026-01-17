package com.backend.trackinvest.usuarios.infrastructure.adapter.out.repository;

import com.backend.trackinvest.usuarios.infrastructure.adapter.out.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
