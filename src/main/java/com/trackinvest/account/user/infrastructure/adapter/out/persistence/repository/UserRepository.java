package com.trackinvest.account.user.infrastructure.adapter.out.persistence.repository;

import com.trackinvest.account.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByCognitoId(String cognitoId);
    boolean existsByEmail(String email);
    boolean existsByCognitoId(String cognitoId);
}
