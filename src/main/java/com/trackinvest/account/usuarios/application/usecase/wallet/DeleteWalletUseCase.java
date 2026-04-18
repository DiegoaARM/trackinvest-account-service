package com.trackinvest.account.usuarios.application.usecase.wallet;

import com.trackinvest.account.usuarios.application.ports.in.service.wallet.DeleteWalletPort;
import com.trackinvest.account.usuarios.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.usuarios.application.ports.out.WalletSecurityPort;
import com.trackinvest.account.usuarios.domain.wallet.exception.business.WalletCannotDeleteLastException;
import com.trackinvest.account.usuarios.domain.wallet.exception.business.WalletNotFoundException;
import com.trackinvest.account.usuarios.domain.wallet.models.WalletDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteWalletUseCase implements DeleteWalletPort {

    private final WalletRepositoryPort walletRepository;
    private final WalletSecurityPort walletSecurityPort;

    @Override
    public void execute(String cognitoId, UUID walletId) {
        walletSecurityPort.validateWalletOwnership(walletId, cognitoId);

        WalletDomain wallet = walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);

        if (wallet.getUser().getWalletsList().size() <= 1) {
            throw new WalletCannotDeleteLastException();
        }

        walletRepository.delete(walletId);
    }
}
