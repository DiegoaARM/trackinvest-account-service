package com.trackinvest.account.user.infrastructure.adapter.out.persistence.persistence;

import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.user.infrastructure.adapter.out.persistence.mapper.UserEntityMapper;
import com.trackinvest.account.user.infrastructure.adapter.out.persistence.repository.UserRepository;
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
    public UserDomain save(UserDomain user) {
        return userEntityMapper.toDomain(
                userRepository.save(
                        userEntityMapper.toEntity(user)
                )
        );
    }

    @Override
    public boolean existsByCognitoId(String cognitoId) {
        return userRepository.existsByCognitoId(cognitoId);
    }
}
