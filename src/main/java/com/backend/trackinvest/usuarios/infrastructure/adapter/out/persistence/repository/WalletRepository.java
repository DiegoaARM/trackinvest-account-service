package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.repository;

import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    List<WalletEntity> findByUser(UserEntity user);

    boolean existsByNameAndUserId(String name, UUID userId);
}
