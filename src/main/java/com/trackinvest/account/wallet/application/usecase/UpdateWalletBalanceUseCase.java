package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletBalanceRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.service.UpdateWalletBalancePort;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.InvalidBalanceException;
import com.trackinvest.account.wallet.domain.exception.business.InsufficientBalanceException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateWalletBalanceUseCase implements UpdateWalletBalancePort {

    private final WalletRepositoryPort walletRepository;

    @Override
    public GetWalletResponseDTO execute(String cognitoId, UUID walletId, UpdateWalletBalanceRequestDTO request) {

        WalletDomain wallet = walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);

        validateAmount(request.amount());

        BigDecimal newBalance;
        if (request.isDeposit()) {
            newBalance = wallet.getBalance().add(request.amount());
        } else {
            if (wallet.getBalance().compareTo(request.amount()) < 0) {
                throw new InsufficientBalanceException();
            }
            newBalance = wallet.getBalance().subtract(request.amount());
        }

        wallet.changeBalance(newBalance);
        WalletDomain savedWallet = walletRepository.save(wallet);
        return GetWalletResponseDTO.fromDomain(savedWallet);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBalanceException();
        }
    }
}
