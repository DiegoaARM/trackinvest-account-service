package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.common.domain.service.AuthorizationService;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletBalanceRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.service.UpdateWalletBalancePort;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateWalletBalanceUseCase implements UpdateWalletBalancePort {

    private final WalletRepositoryPort walletRepository;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional
    public GetWalletResponseDTO execute(UUID userId, UUID walletId, UpdateWalletBalanceRequestDTO request) {
        WalletDomain wallet = walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);

        validateRules(userId, wallet);

        if (request.isDeposit()) {
            wallet.deposit(request.amount());
        } else {
            wallet.withdraw(request.amount());
        }

        WalletDomain savedWallet = walletRepository.save(wallet);
        return GetWalletResponseDTO.fromDomain(savedWallet);
    }

    private void validateRules(UUID userId, WalletDomain wallet) {
        authorizationService.verifyOwner(wallet.getUser().getId(), userId, "wallet");
    }
}
