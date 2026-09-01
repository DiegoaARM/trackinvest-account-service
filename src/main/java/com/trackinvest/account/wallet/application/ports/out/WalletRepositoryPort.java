package com.trackinvest.account.wallet.application.ports.out;

import com.trackinvest.account.wallet.domain.models.WalletDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {

    Optional<WalletDomain> findByIdAndUserId(UUID id, UUID userId);
    boolean existsByNameAndUserId(String name, UUID userId);
    WalletDomain save(WalletDomain wallet);
    void deleteByIdAndUserId(UUID id, UUID userId);
    List<WalletDomain> findByUserId(UUID userId);
    long countByUserId(UUID userId);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}
