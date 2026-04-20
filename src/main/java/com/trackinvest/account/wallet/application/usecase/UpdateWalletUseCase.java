package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.service.UpdateWalletPort;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletNameDuplicateException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.rules.WalletNameValidRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateWalletUseCase implements UpdateWalletPort {

    private final WalletRepositoryPort walletRepository;

    @Override
    public GetWalletResponseDTO execute(String cognitoId, UUID walletId, UpdateWalletRequestDTO request) {

        WalletDomain wallet = walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);

        if (request.name() != null) {
            WalletNameValidRule.validate(request.name());
            if (!request.name().equals(wallet.getName()) &&
                walletRepository.existsByNameAndUserId(request.name(), wallet.getUser().getId())) {
                throw new WalletNameDuplicateException();
            }
            wallet.changeName(request.name());
        }


        WalletDomain savedWallet = walletRepository.save(wallet);
        return GetWalletResponseDTO.fromDomain(savedWallet);
    }
}
