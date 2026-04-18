package com.trackinvest.account.usuarios.application.usecase.wallet;

import com.trackinvest.account.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.trackinvest.account.usuarios.application.ports.in.dto.wallet.UpdateWalletBalanceRequestDTO;
import com.trackinvest.account.usuarios.application.ports.in.service.wallet.UpdateWalletBalancePort;
import com.trackinvest.account.usuarios.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.usuarios.application.ports.out.WalletSecurityPort;
import com.trackinvest.account.usuarios.domain.wallet.exception.business.InvalidBalanceException;
import com.trackinvest.account.usuarios.domain.wallet.exception.business.InsufficientBalanceException;
import com.trackinvest.account.usuarios.domain.wallet.exception.business.WalletNotFoundException;
import com.trackinvest.account.usuarios.domain.wallet.models.WalletDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateWalletBalanceUseCase implements UpdateWalletBalancePort {

    private final WalletRepositoryPort walletRepository;
    private final WalletSecurityPort walletSecurityPort;

    @Override
    public GetWalletResponseDTO execute(String cognitoId, UUID walletId, UpdateWalletBalanceRequestDTO request) {
        walletSecurityPort.validateWalletOwnership(walletId, cognitoId);

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
