package com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.persistence;

import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.mapper.WalletEntityMapper;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public boolean existsByNameAndUserId(String name, UUID userId) {
        return walletRepository.existsByNameAndUserId(name, userId);
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
    public void delete(UUID id) {
        walletRepository.deleteById(id);
    }

    @Override
    public List<WalletDomain> findByUserId(UUID userId) {
        return walletRepository.findByUser_Id(userId)
                .stream()
                .map(walletEntityMapper::toDomain)
                .toList();
    }

    @Override
    public long countByUserId(UUID userId) {
        return walletRepository.countByUser_Id(userId);
    }

    @Override
    public boolean existsByIdAndUserId(UUID id, UUID userId) {
        return walletRepository.existsByIdAndUserId(id, userId);
    }
}
