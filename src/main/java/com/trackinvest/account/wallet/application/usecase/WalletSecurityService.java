package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.application.ports.out.WalletSecurityPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletUnauthorizedException;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletSecurityService implements WalletSecurityPort {

    private final WalletRepositoryPort walletRepository;

    @Override
    public void validateWalletOwnership(UUID walletId, String cognitoId) {
        WalletDomain wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException());

        if (!wallet.getUser().getCognitoId().equals(cognitoId)) {
            throw new WalletUnauthorizedException();
        }
    }
}
