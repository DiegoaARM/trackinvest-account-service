package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.repository;

import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.entity.WalletEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    List<WalletEntity> findByUser(User user);
}
