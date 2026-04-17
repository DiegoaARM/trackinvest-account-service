package com.backend.trackinvest.usuarios.application.usecase.wallet;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.UpdateWalletRequestDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.wallet.UpdateWalletPort;
import com.backend.trackinvest.usuarios.application.ports.out.WalletRepositoryPort;
import com.backend.trackinvest.usuarios.application.ports.out.WalletSecurityPort;
import com.backend.trackinvest.usuarios.domain.wallet.exception.business.WalletNameDuplicateException;
import com.backend.trackinvest.usuarios.domain.wallet.exception.business.WalletNotFoundException;
import com.backend.trackinvest.usuarios.domain.wallet.models.WalletDomain;
import com.backend.trackinvest.usuarios.domain.wallet.rules.WalletNameValidRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateWalletUseCase implements UpdateWalletPort {

    private final WalletRepositoryPort walletRepository;
    private final WalletSecurityPort walletSecurityPort;

    @Override
    public GetWalletResponseDTO execute(String cognitoId, UUID walletId, UpdateWalletRequestDTO request) {
        walletSecurityPort.validateWalletOwnership(walletId, cognitoId);

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
