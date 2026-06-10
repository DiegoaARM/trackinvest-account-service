package com.trackinvest.account.user.application.usecase.user;

import com.trackinvest.account.common.application.ports.out.EventPublisherPort;
import com.trackinvest.account.user.application.ports.in.service.user.SyncUserPort;
import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.event.UserCreatedEvent;
import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.models.valueobjects.CurrencyTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncUserUseCase implements SyncUserPort {

    private final UserRepositoryPort userRepository;
    private final EventPublisherPort eventPublisher;

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

        WalletDomain initialWallet = WalletDomain.create(
                UUID.randomUUID(),
                "Initial Wallet",
                newUser.getId(),
                CurrencyTypeEnum.USD
        );

        newUser.addWallet(initialWallet);
        userRepository.save(newUser);

        eventPublisher.publish(new UserCreatedEvent(
                newUser.getId().toString(),
                newUser.getEmail(),
                newUser.getFullname()
        ));
    }
}
