package com.backend.trackinvest.usuarios.application.usecase.user;

import com.backend.trackinvest.usuarios.application.ports.in.dto.user.GetUserProfileResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.user.GetUserProfilePort;
import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.domain.exception.user.business.UserNotFoundException;
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
