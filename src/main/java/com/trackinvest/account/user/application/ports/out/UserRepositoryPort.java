package com.trackinvest.account.user.application.ports.out;

import com.trackinvest.account.user.domain.models.UserDomain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    Optional<UserDomain> findById(UUID id);

    UserDomain save(UserDomain user);

    boolean existsByCognitoId(String cognitoId);
}
