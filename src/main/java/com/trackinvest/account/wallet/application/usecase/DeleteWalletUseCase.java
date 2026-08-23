package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.service.DeleteWalletPort;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletCannotDeleteLastException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteWalletUseCase implements DeleteWalletPort {

    private final WalletRepositoryPort walletRepository;

    @Override
    @Transactional
    public void execute(UUID userId, UUID walletId) {
        validateRules(userId, walletId);
        walletRepository.delete(walletId);
    }

    private void validateRules(UUID userId, UUID walletId) {
        if (!walletRepository.existsByIdAndUserId(walletId, userId)) {
            throw new WalletNotFoundException();
        }
        if (walletRepository.countByUserId(userId) <= 1) {
            throw new WalletCannotDeleteLastException();
        }
    }
}
