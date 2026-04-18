package com.trackinvest.account.usuarios.application.usecase.wallet;

import com.trackinvest.account.usuarios.application.ports.in.dto.wallet.CreateWalletRequestDTO;
import com.trackinvest.account.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.trackinvest.account.usuarios.application.ports.in.service.wallet.CreateWalletPort;
import com.trackinvest.account.usuarios.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.usuarios.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.usuarios.domain.user.exception.business.UserNotFoundException;
import com.trackinvest.account.usuarios.domain.wallet.exception.business.WalletNameDuplicateException;
import com.trackinvest.account.usuarios.domain.user.models.UserDomain;
import com.trackinvest.account.usuarios.domain.wallet.models.WalletDomain;
import com.trackinvest.account.usuarios.domain.wallet.rules.WalletNameValidRule;
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

        WalletNameValidRule.validate(wallet.name());

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
