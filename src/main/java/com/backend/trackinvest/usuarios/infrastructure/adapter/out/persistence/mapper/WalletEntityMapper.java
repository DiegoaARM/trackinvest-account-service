package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.mapper;

import com.backend.trackinvest.usuarios.domain.wallet.models.WalletDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.entity.WalletEntity;
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
