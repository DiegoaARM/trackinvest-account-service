package com.trackinvest.account.user.application.usecase.user;

import com.trackinvest.account.user.application.ports.in.dto.user.GetUserProfileResponseDTO;
import com.trackinvest.account.user.application.ports.in.service.user.GetUserProfilePort;
import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.exception.business.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCase implements GetUserProfilePort {

    private final UserRepositoryPort userRepository;

    @Override
    public GetUserProfileResponseDTO execute(String cognitoId) {
        return userRepository.findByCognitoId(cognitoId)
                .map(GetUserProfileResponseDTO::fromDomain)
                .orElseThrow(UserNotFoundException::new);
    }
}
