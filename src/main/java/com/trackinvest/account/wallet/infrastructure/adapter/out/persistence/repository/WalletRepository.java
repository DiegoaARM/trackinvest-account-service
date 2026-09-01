package com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.repository;

import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.entity.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    boolean existsByNameAndUserId(String name, UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<WalletEntity> findByIdAndUser_Id(UUID id, UUID userId);

    List<WalletEntity> findByUser_Id(UUID userId);

    void deleteByIdAndUser_Id(UUID id, UUID userId);

    long countByUser_Id(UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
