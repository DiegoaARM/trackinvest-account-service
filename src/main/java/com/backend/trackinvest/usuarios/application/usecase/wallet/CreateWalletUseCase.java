package com.backend.trackinvest.usuarios.application.usecase.wallet;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.wallet.CreateWalletPort;
import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.application.ports.out.WalletRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateWalletUseCase implements CreateWalletPort {

    private final UserRepositoryPort userRepository;
    private final WalletRepositoryPort walletRepository;

    @Override
    public void execute(String userCognitoId, GetWalletResponseDTO wallet) {

    }
}
