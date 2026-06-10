package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.common.application.ports.out.EventPublisherPort;
import com.trackinvest.account.wallet.application.ports.in.dto.CreateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.service.CreateWalletPort;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.event.WalletCreatedEvent;
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
    private final EventPublisherPort eventPublisher;

    @Override
    public GetWalletResponseDTO execute(UUID userId, CreateWalletRequestDTO wallet) {
        validateRules(userId, wallet);

        WalletDomain walletDomain = WalletDomain.create(
                UUID.randomUUID(),
                wallet.name(),
                userId,
                wallet.currency()
        );

        WalletDomain savedWallet = walletRepository.save(walletDomain);

        eventPublisher.publish(new WalletCreatedEvent(
                savedWallet.getId().toString(),
                userId.toString(),
                savedWallet.getName(),
                savedWallet.getCurrency().name(),
                savedWallet.getBalance()
        ));

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
