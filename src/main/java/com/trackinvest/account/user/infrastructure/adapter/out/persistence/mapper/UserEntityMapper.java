package com.trackinvest.account.user.infrastructure.adapter.out.persistence.mapper;

import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.entity.WalletEntity;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    default UserEntity toEntity(UserDomain domain) {
        if (domain == null) return null;
        List<WalletEntity> wallets = null;
        if (domain.getWalletsList() != null) {
            wallets = domain.getWalletsList().stream()
                    .map(walletDomain -> WalletEntity.builder()
                            .id(walletDomain.getId())
                            .name(walletDomain.getName())
                            .balance(walletDomain.getBalance())
                            .currency(walletDomain.getCurrency())
                            .createdAt(walletDomain.getCreatedAt())
                            .updatedAt(walletDomain.getUpdatedAt())
                            .build())
                    .toList();
        }
        UserEntity userEntity = UserEntity.builder()
                .id(domain.getId())
                .cognitoId(domain.getCognitoId())
                .fullname(domain.getFullname())
                .email(domain.getEmail())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .walletsList(wallets)
                .build();
        // Set the user reference in each wallet
        if (userEntity.getWalletsList() != null) {
            userEntity.getWalletsList().forEach(wallet -> wallet.setUser(userEntity));
        }
        return userEntity;
    }

    default UserDomain toDomain(UserEntity entity) {
        if (entity == null) return null;
        List<WalletDomain> walletDomains = null;
        if (Hibernate.isInitialized(entity.getWalletsList()) && entity.getWalletsList() != null) {
            walletDomains = entity.getWalletsList().stream()
                    .map(w -> WalletDomain.from(
                            w.getId(),
                            w.getName(),
                            w.getBalance(),
                            w.getCurrency(),
                            w.getCreatedAt(),
                            w.getUpdatedAt()
                    )).toList();
        }
        return UserDomain.from(
                entity.getId(),
                entity.getCognitoId(),
                entity.getFullname(),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                walletDomains
        );
    }
}
