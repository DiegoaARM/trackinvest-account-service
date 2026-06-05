 package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.exception.business.UserNotFoundException;
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
    private final UserRepositoryPort userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GetWalletResponseDTO> execute(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        return walletRepository.findByUserId(userId)
                .stream()
                .map(GetWalletResponseDTO::fromDomain)
                .toList();
    }
}
