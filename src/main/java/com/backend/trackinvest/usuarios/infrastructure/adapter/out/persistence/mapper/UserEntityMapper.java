package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.mapper;

import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Name;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.domain.models.wallet.WalletDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Para que Spring lo inyecte como un Bean
public interface UserEntityMapper {

    @Mapping(target = "firstName", source = "name.firstName")
    @Mapping(target = "middleName", source = "name.middleName")
    @Mapping(target = "lastName", source = "name.lastName")
    @Mapping(target = "secondLastName", source = "name.secondLastName")
    @Mapping(target = "email", source = "email.value")
    UserEntity toEntity(UserDomain domain);

    default UserDomain toDomain(UserEntity entity) {
        if (entity == null) return null;

        // Si las billeteras están cargadas (JOIN FETCH), usamos el 'from' largo
        if (Hibernate.isInitialized(entity.getWalletsList()) && entity.getWalletsList() != null) {
            return UserDomain.from(
                    entity.getId(),
                    entity.getCognitoId(),
                    new Name(entity.getFirstName(), entity.getMiddleName(), entity.getLastName(), entity.getSecondLastName()),
                    new Email(entity.getEmail()),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt(),
                    entity.getWalletsList().stream().map(
                            w -> WalletDomain.from(
                                    w.getId(),
                                    w.getName(),
                                    w.getBalance(),
                                    w.getCurrency(),
                                    w.getCreatedAt(),
                                    w.getUpdatedAt()
                    )).toList()
            );
        }

        // Si NO están cargadas, usamos el 'from' corto (lista vacía por defecto internamente)
        return UserDomain.from(
                entity.getId(),
                entity.getCognitoId(),
                new Name(entity.getFirstName(), entity.getMiddleName(), entity.getLastName(), entity.getSecondLastName()),
                new Email(entity.getEmail()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
