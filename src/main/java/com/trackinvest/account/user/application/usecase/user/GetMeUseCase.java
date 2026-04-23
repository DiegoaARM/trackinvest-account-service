package com.trackinvest.account.user.application.usecase.user;

import com.trackinvest.account.user.application.ports.in.dto.user.GetUserResponseDTO;
import com.trackinvest.account.user.application.ports.in.service.user.GetMePort;
import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.exception.business.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMeUseCase implements GetMePort {

    private final UserRepositoryPort userRepository;

    @Override
    public GetUserResponseDTO execute(String cognitoId) {
        return userRepository.findByCognitoId(cognitoId)
                .map(GetUserResponseDTO::fromDomain)
                .orElseThrow(UserNotFoundException::new);
    }
}
