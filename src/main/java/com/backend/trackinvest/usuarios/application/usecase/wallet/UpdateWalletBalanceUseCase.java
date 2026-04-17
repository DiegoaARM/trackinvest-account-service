package com.backend.trackinvest.usuarios.application.usecase.wallet;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.UpdateWalletBalanceRequestDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.wallet.UpdateWalletBalancePort;
import com.backend.trackinvest.usuarios.application.ports.out.WalletRepositoryPort;
import com.backend.trackinvest.usuarios.application.ports.out.WalletSecurityPort;
import com.backend.trackinvest.usuarios.domain.exception.wallet.business.InvalidBalanceException;
import com.backend.trackinvest.usuarios.domain.exception.wallet.business.InsufficientBalanceException;
import com.backend.trackinvest.usuarios.domain.exception.wallet.business.WalletNotFoundException;
import com.backend.trackinvest.usuarios.domain.models.wallet.WalletDomain;
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
