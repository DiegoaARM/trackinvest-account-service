package com.trackinvest.account.user.application.usecase.user;

import com.trackinvest.account.user.application.ports.in.service.user.ChangeNamePort;
import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.exception.business.UserNotFoundException;
import com.trackinvest.account.user.domain.models.UserDomain;
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
