package com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.mapper;

import com.trackinvest.account.user.infrastructure.adapter.out.persistence.mapper.UserEntityMapper;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserEntityMapper.class})
public interface WalletEntityMapper {

    // MapStruct detectará el @Builder en WalletDomain y lo usará automáticamente
    WalletDomain toDomain(WalletEntity entity);

    // Para la entidad, asumiendo que también usas @Builder o constructor público
    @Mapping(target = "user", source = "user")
    WalletEntity toEntity(WalletDomain domain);
}
