package com.backend.trackinvest.usuarios.application.ports.out;

import com.backend.trackinvest.usuarios.domain.models.wallet.WalletDomain;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {

    Optional<WalletDomain> findById(UUID id);

    WalletDomain save(WalletDomain wallet);
}
