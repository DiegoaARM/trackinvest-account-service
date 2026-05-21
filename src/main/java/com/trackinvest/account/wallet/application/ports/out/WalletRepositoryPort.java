package com.trackinvest.account.wallet.application.ports.out;

import com.trackinvest.account.wallet.domain.models.WalletDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {

    Optional<WalletDomain> findById(UUID id);
    boolean existsById(UUID id);
    boolean existsByNameAndUserId(String name, UUID userId);
    WalletDomain save(WalletDomain wallet);
    void delete(UUID id);
    List<WalletDomain> findByUserId(UUID userId);
    long countByUserId(UUID userId);
}
