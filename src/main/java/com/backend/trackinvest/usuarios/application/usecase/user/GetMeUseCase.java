package com.backend.trackinvest.usuarios.application.usecase.user;

import com.backend.trackinvest.usuarios.application.ports.in.dto.user.GetUserResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.user.GetMePort;
import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.domain.exception.business.UserNotFoundException;
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
