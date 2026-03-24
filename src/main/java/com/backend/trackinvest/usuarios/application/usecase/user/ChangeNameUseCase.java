package com.backend.trackinvest.usuarios.application.usecase.user;

import com.backend.trackinvest.usuarios.application.ports.in.service.user.ChangeNamePort;
import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.domain.exception.business.UserNotFoundException;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
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
