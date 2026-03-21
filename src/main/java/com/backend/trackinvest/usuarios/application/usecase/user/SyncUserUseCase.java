package com.backend.trackinvest.usuarios.application.usecase.user;

import com.backend.trackinvest.usuarios.application.ports.in.service.user.SyncUserPort;
import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Name;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncUserUseCase implements SyncUserPort {

    private final UserRepositoryPort userRepository;

    @Override
    public void execute(String cognitoId, String email, String fullname) {
        if(!userRepository.existsByCognitoId(cognitoId)) {
            UserDomain newUser = UserDomain.create(
                    UUID.randomUUID(),
                    cognitoId,
                    Name.fromFullString(fullname),
                    new Email(email)
            );
        userRepository.save(newUser);
        }
    }
}
