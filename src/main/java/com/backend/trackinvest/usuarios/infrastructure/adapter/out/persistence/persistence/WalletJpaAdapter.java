package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.persistence;

import com.backend.trackinvest.usuarios.application.ports.out.WalletRepositoryPort;
import com.backend.trackinvest.usuarios.domain.wallet.models.WalletDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.mapper.WalletEntityMapper;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletJpaAdapter implements WalletRepositoryPort {

    private final WalletRepository walletRepository;
    private final WalletEntityMapper walletEntityMapper;

    @Override
    public Optional<WalletDomain> findById(UUID id) {
        return walletRepository.findById(id)
                .map(walletEntityMapper::toDomain);
    }

    @Override
    public WalletDomain save(WalletDomain wallet) {
        return walletEntityMapper.toDomain(
                walletRepository.save(
                        walletEntityMapper.toEntity(wallet)
                )
        );
    }

    @Override
    public boolean existsByNameAndUserId(String name, UUID userId) {
        return walletRepository.existsByNameAndUserId(name, userId);
    }

    @Override
    public void delete(UUID id) {
        walletRepository.deleteById(id);
    }
}
