package com.backend.trackinvest.usuarios.application.ports.in.mapper;

import com.backend.trackinvest.usuarios.application.ports.in.dto.CrearUsuarioDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.ObtenerUsuarioDTO;
import com.backend.trackinvest.usuarios.domain.models.UsuarioDomain;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Nombre;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Password;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UsuarioDTOMapper {

    // El UUID NO se genera aquí, se pasará como parámetro o se ignorará
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "nombre", expression = "java(mapNombre(dto))")
    @Mapping(target = "email", expression = "java(new Email(dto.email()))")
    @Mapping(target = "password", expression = "java(new Password(dto.password()))")
    public abstract UsuarioDomain toDomain(CrearUsuarioDTO dto);

    @Mapping(target = "nombreCompleto", source = "nombre.nombreCompleto")
    @Mapping(target = "email", source = "email.value")
    public abstract ObtenerUsuarioDTO toDto(UsuarioDomain domain);

    // Lógica personalizada para construir el VO Nombre
    public default Nombre mapNombre(CrearUsuarioDTO dto) {
        String[] partesNombres = dto.nombres().trim().split("\\s+", 2);
        String[] partesApellidos = dto.apellidos().trim().split("\\s+", 2);

        return new Nombre(
                partesNombres[0],
                partesNombres.length > 1 ? partesNombres[1] : null,
                partesApellidos[0],
                partesApellidos.length > 1 ? partesApellidos[1] : null
        );
    }



}
