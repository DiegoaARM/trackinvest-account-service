package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.persistence;

import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.domain.user.models.UserDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.mapper.UserEntityMapper;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepositoryPort {


    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Optional<UserDomain> findById(UUID id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<UserDomain> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<UserDomain> findByCognitoId(String congitoId) {
        return userRepository.findByCognitoId(congitoId)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public UserDomain save(UserDomain user) {
        return userEntityMapper.toDomain(
                userRepository.save(
                        userEntityMapper.toEntity(user)
                )
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByCognitoId(String cognitoId) {
        return userRepository.existsByCognitoId(cognitoId);
    }
}
