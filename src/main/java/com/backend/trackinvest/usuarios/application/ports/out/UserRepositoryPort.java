package com.backend.trackinvest.usuarios.application.ports.out;

import com.backend.trackinvest.usuarios.domain.user.models.UserDomain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    Optional<UserDomain> findById(UUID id);

    Optional<UserDomain> findByEmail(String email);

    Optional<UserDomain> findByCognitoId(String cognitoId);

    UserDomain save(UserDomain user);

    boolean existsByEmail(String email);

    boolean existsByCognitoId(String cognitoId);
}
