package com.trackinvest.account.wallet.application.ports.out;

import com.trackinvest.account.wallet.domain.models.WalletDomain;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {

    Optional<WalletDomain> findById(UUID id);

    WalletDomain save(WalletDomain wallet);

    boolean existsByNameAndUserId(String name, UUID userId);

    void delete(UUID id);
}
