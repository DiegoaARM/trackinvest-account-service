package com.trackinvest.account.user.infrastructure.adapter.out.persistence.repository;

import com.trackinvest.account.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @EntityGraph(attributePaths = {"walletsList"})
    @Query("SELECT u FROM UserEntity u WHERE u.id = :id") // Loads wallets in the same SQL query
    Optional<UserEntity> findByIdWithWallets(UUID id);

    @Query("SELECT u.id FROM UserEntity u WHERE u.cognitoId = :cognitoId")
    Optional<UUID> findIdByCognitoId(@Param("cognitoId") String cognitoId);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByCognitoId(String cognitoId);

    boolean existsByEmail(String email);

    boolean existsByCognitoId(String cognitoId);
}
