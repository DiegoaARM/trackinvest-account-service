package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.dto.CreateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.service.CreateWalletPort;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletMaxNumberException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNameDuplicateException;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.rules.WalletNameValidRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateWalletUseCase implements CreateWalletPort {

    private final WalletRepositoryPort walletRepository;

    @Override
    public GetWalletResponseDTO execute(UUID userId, CreateWalletRequestDTO wallet) {
        validateRules(userId, wallet);

        if (walletRepository.existsByNameAndUserId(wallet.name(), userId)) {
            throw new WalletNameDuplicateException();
        }

        WalletDomain walletDomain = WalletDomain.create(
                UUID.randomUUID(),
                wallet.name(),
                userId,
                wallet.currency()
        );

        WalletDomain savedWallet = walletRepository.save(walletDomain);
        return GetWalletResponseDTO.fromDomain(savedWallet);
    }

    private void validateRules(UUID userId, CreateWalletRequestDTO wallet) {

        WalletNameValidRule.validate(wallet.name());

        long currentWalletCount = walletRepository.countByUserId(userId);
        if (currentWalletCount >= 10) {
            throw new WalletMaxNumberException();
        }

        if (walletRepository.existsByNameAndUserId(wallet.name(), userId)) {
            throw new WalletNameDuplicateException();
        }
    }
}
