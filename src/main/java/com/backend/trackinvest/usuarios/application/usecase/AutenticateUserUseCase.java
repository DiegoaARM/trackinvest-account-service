package com.backend.trackinvest.usuarios.application.usecase;

import com.backend.trackinvest.usuarios.application.ports.in.dto.LoginDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.AuthResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.AutenticateUserPort;
import com.backend.trackinvest.usuarios.application.ports.out.JwtProviderPort;
import com.backend.trackinvest.usuarios.application.ports.out.PasswordEncoderPort;
import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.domain.exception.business.EmailNotFoundException;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AutenticateUserUseCase implements AutenticateUserPort {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtProviderPort jwtProvider;

    @Override
    public AuthResponseDTO execute(LoginDTO dto) {
        UserDomain user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new EmailNotFoundException(dto.email()));

        if (!passwordEncoder.matches(dto.password(), user.getPassword().value())) {
            throw new EmailNotFoundException(dto.email());
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail().value(),
                null,
                Collections.emptyList()
        );

        String token = jwtProvider.createToken(auth);

        return new AuthResponseDTO(token, user.getEmail().value(), user.getName().fullName());
    }
}
