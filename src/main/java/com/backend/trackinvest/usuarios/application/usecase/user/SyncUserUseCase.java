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
    private final CreateWalletPort createWalletPort;

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

        // 3. Crear la Billetera Inicial usando el Factory de Wallet
        // Nota: Le pasamos 'newUser' para establecer la relación bidireccional
        WalletDomain initialWallet = WalletDomain.create(
                UUID.randomUUID(),
                "Initial Wallet",
                newUser,
                CurrencyTypeEnum.USD // Moneda por defecto de bienvenida
        );

        // Esto ejecuta las validaciones de nombre, límites, etc.
        newUser.addWallet(initialWallet);

        // 5. Persistir el Agregado Completo
        // Al guardar el usuario, JPA detectará la nueva billetera en la lista
        // y la insertará gracias al Cascade
        userRepository.save(newUser);
    }
}
