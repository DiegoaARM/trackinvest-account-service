package com.trackinvest.account.user.infrastructure.adapter.out.persistence.mapper;

import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.entity.WalletEntity;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(target = "walletsList", ignore = true)
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

    default UserEntity toEntityWithWallets(UserDomain domain) {
        if (domain == null) return null;

        UserEntity userEntity = toEntity(domain);

        if (domain.getWalletsList() != null) {
            List<WalletEntity> wallets = domain.getWalletsList().stream()
                    .map(w -> {
                        WalletEntity wallet = new WalletEntity();
                        wallet.setId(w.getId());
                        wallet.setName(w.getName());
                        wallet.setBalance(w.getBalance());
                        wallet.setCurrency(w.getCurrency());
                        wallet.setCreatedAt(w.getCreatedAt());
                        wallet.setUpdatedAt(w.getUpdatedAt());

                        // 🔥 clave: setear solo referencia, no mapear user completo
                        wallet.setUser(userEntity);

                        return wallet;
                    }).toList();

            userEntity.setWalletsList(wallets);
        }

        return userEntity;
    }
}
