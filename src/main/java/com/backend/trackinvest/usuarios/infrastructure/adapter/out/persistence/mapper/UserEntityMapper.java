package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.mapper;

import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Name;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Para que Spring lo inyecte como un Bean
public interface UserEntityMapper {

    @Mapping(target = "firstName", source = "name.firstName")
    @Mapping(target = "middleName", source = "name.middleName")
    @Mapping(target = "lastName", source = "name.lastName")
    @Mapping(target = "secondLastName", source = "name.secondLastName")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "cognitoId", source = "cognito_id")
    UserEntity toEntity(UserDomain domain);

    default UserDomain toDomain(UserEntity entity) {
        if (entity == null) return null;

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
