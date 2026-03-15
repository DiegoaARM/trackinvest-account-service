package com.backend.trackinvest.usuarios.application.ports.out;

import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    Optional<UserDomain> findById(UUID id);

    Optional<UserDomain> findByEmail(String email);

    UserDomain save(UserDomain user);

    boolean existsByEmail(String email);
}
