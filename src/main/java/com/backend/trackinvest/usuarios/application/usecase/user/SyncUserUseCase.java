package com.backend.trackinvest.usuarios.application.usecase.user;

import com.backend.trackinvest.usuarios.application.ports.in.service.user.SyncUserPort;
import com.backend.trackinvest.usuarios.application.ports.in.service.wallet.CreateWalletPort;
import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.domain.models.wallet.WalletDomain;
import com.backend.trackinvest.usuarios.domain.models.wallet.valueobjects.CurrencyTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncUserUseCase implements SyncUserPort {

    private final UserRepositoryPort userRepository;

    @Override
    public void execute(String cognitoId, String email, String fullname) {
        if(userRepository.existsByCognitoId(cognitoId)) {
            return;
        }

        UserDomain newUser = UserDomain.create(
                UUID.randomUUID(),
                cognitoId,
                fullname,
                email
        );

        UserDomain userWithoutWallets = UserDomain.create(
                newUser.getId(),
                cognitoId,
                fullname,
                email
        );

        WalletDomain initialWallet = WalletDomain.create(
                UUID.randomUUID(),
                "Initial Wallet",
                userWithoutWallets,
                CurrencyTypeEnum.USD //initial currency
        );

        newUser.addWallet(initialWallet);
        userRepository.save(newUser);
    }
}
