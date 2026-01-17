package com.backend.trackinvest.usuarios.infrastructure.adapter.out.mapper;

import com.backend.trackinvest.usuarios.domain.models.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Nombre;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Password;
import com.backend.trackinvest.usuarios.domain.models.UsuarioDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Para que Spring lo inyecte como un Bean
public interface UsuarioEntityMapper {

    @Mapping(target = "primerNombre", source = "nombre.primerNombre")
    @Mapping(target = "segundoNombre", source = "nombre.segundoNombre")
    @Mapping(target = "primerApellido", source = "nombre.primerApellido")
    @Mapping(target = "segundoApellido", source = "nombre.segundoApellido")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "password", source = "password.value")
    UsuarioEntity toEntity(UsuarioDomain domain);

    default UsuarioDomain toDomain(UsuarioEntity entity) {
        if (entity == null) return null;

        return UsuarioDomain.from(
                entity.getId(),
                new Nombre(entity.getPrimerNombre(), entity.getSegundoNombre(), entity.getPrimerApellido(), entity.getSegundoApellido()),
                new Email(entity.getEmail()),
                new Password(entity.getPassword()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
