package com.trackinvest.account.user.infrastructure.adapter.out.persistence.mapper;

import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.mapper.WalletEntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WalletEntityMapper.class})
public interface UserEntityMapper {

    UserEntity toEntity(UserDomain domain);

    default UserDomain toDomain(UserEntity entity) {
        if (entity == null) return null;

        return UserDomain.from(
                entity.getId(),
                entity.getCognitoId(),
                entity.getFullname(),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
                //mapWallets(entity.getWalletsList())
        );
    }
}
