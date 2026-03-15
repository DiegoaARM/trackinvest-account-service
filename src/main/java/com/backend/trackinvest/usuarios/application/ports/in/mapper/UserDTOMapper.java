package com.backend.trackinvest.usuarios.application.ports.in.mapper;

import com.backend.trackinvest.usuarios.application.ports.in.dto.RegisterUserDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.GetUserDTO;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", expression = "java(mapName(dto))")
    @Mapping(target = "email", expression = "java(new Email(dto.email()))")
    @Mapping(target = "password", expression = "java(new Password(dto.password()))")
    public abstract UserDomain toDomain(RegisterUserDTO dto);

    @Mapping(target = "fullName", source = "name.fullName")
    @Mapping(target = "email", source = "email.value")
    public abstract GetUserDTO toDto(UserDomain domain);

    public default Name mapName(RegisterUserDTO dto) {
        String[] firstNamePart = dto.firstName().trim().split("\\s+", 2);
        String[] lastNamePart = dto.lastName().trim().split("\\s+", 2);

        return new Name(
                firstNamePart[0],
                firstNamePart.length > 1 ? firstNamePart[1] : null,
                lastNamePart[0],
                lastNamePart.length > 1 ? lastNamePart[1] : null
        );
    }



}
