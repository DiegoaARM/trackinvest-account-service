package com.backend.trackinvest.usuarios.application.usecase.wallet;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.CreateWalletRequestDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.wallet.CreateWalletPort;
import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.application.ports.out.WalletRepositoryPort;
import com.backend.trackinvest.usuarios.domain.exception.user.business.UserNotFoundException;
import com.backend.trackinvest.usuarios.domain.exception.wallet.business.WalletNameDuplicateException;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.domain.models.wallet.WalletDomain;
import com.backend.trackinvest.usuarios.domain.rules.wallet.NameWalletValidRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateWalletUseCase implements CreateWalletPort {

    private final UserRepositoryPort userRepository;
    private final WalletRepositoryPort walletRepository;

    @Override
    public GetWalletResponseDTO execute(String cognitoId, CreateWalletRequestDTO wallet) {
        UserDomain user = userRepository.findByCognitoId(cognitoId)
                .orElseThrow(UserNotFoundException::new);

        NameWalletValidRule.validate(wallet.name());

        if (walletRepository.existsByNameAndUserId(wallet.name(), user.getId())) {
            throw new WalletNameDuplicateException();
        }

        WalletDomain walletDomain = WalletDomain.create(
                UUID.randomUUID(),
                wallet.name(),
                user,
                wallet.currency()
        );
        // 3. LA AÑADIMOS al usuario (Para que pase sus validaciones)
        user.addWallet(walletDomain);

        WalletDomain savedWallet = walletRepository.save(walletDomain);
        return GetWalletResponseDTO.fromDomain(savedWallet);
    }
}
