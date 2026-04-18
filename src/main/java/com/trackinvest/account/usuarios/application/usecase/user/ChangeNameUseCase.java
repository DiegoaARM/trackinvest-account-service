package com.trackinvest.account.usuarios.application.usecase.user;

import com.trackinvest.account.usuarios.application.ports.in.service.user.ChangeNamePort;
import com.trackinvest.account.usuarios.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.usuarios.domain.user.exception.business.UserNotFoundException;
import com.trackinvest.account.usuarios.domain.user.models.UserDomain;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeNameUseCase implements ChangeNamePort {

    private final UserRepositoryPort userRepository;

    @Override
    @Transactional
    public void changeName(String cognitoId, String newFullName) {
        UserDomain user = userRepository.findByCognitoId(cognitoId)
                .orElseThrow(UserNotFoundException::new);
    }
}
