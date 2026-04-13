package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.mapper;

import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.domain.models.wallet.WalletDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntity toEntity(UserDomain domain);

    default UserDomain toDomain(UserEntity entity) {
        if (entity == null) return null;

        List<WalletDomain> walletDomains = null;

        // Verificamos si las billeteras están inicializadas para evitar LazyInitializationException
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
