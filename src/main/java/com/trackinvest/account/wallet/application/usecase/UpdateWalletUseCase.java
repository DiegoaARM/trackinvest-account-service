package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.service.UpdateWalletPort;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletNameDuplicateException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateWalletUseCase implements UpdateWalletPort {

    private final WalletRepositoryPort walletRepository;

    @Override
    @Transactional
    public GetWalletResponseDTO execute(UUID userId, UUID walletId, UpdateWalletRequestDTO request) {
        validateOwnership(walletId, userId);

        WalletDomain wallet = walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);

        validateRules(userId, wallet, request);
        wallet.changeName(request.name());

        WalletDomain savedWallet = walletRepository.save(wallet);
        return GetWalletResponseDTO.fromDomain(savedWallet);
    }

    private void validateOwnership(UUID walletId, UUID userId) {
        if (!walletRepository.existsByIdAndUserId(walletId, userId)) {
            throw new WalletNotFoundException();
        }
    }

    private void validateRules(UUID userId, WalletDomain wallet, UpdateWalletRequestDTO request) {
        if (request.name() != null
                && !request.name().equals(wallet.getName())
                && walletRepository.existsByNameAndUserId(request.name(), userId)) {
            throw new WalletNameDuplicateException();
        }
    }
}
