 package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.service.GetAllWalletsPort;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAllWalletsUseCase implements GetAllWalletsPort {

    private final WalletRepositoryPort walletRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GetWalletResponseDTO> execute(UUID userId) {
        return walletRepository.findByUserId(userId)
                .stream()
                .map(GetWalletResponseDTO::fromDomain)
                .toList();
    }
}
