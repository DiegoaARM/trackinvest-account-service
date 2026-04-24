package com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.mapper;

import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletEntityMapper {

    @Mapping(target = "user", ignore = true)
    WalletDomain toDomain(WalletEntity entity);

    @Mapping(target = "user", ignore = true)
    WalletEntity toEntity(WalletDomain domain);
}
