package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.persistence;

import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.mapper.UserEntityMapper;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserJpaAdapter implements UserRepositoryPort {


    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    public UserJpaAdapter(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

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
}
