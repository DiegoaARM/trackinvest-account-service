package com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.repository;

import com.trackinvest.account.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    boolean existsByNameAndUserId(String name, UUID userId);

    List<WalletEntity> findByUser_Id(UUID userId);

    long countByUser_Id(UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
