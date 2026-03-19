package com.backend.trackinvest.usuarios.application.ports.in.mapper;

import com.backend.trackinvest.usuarios.application.ports.in.dto.RegisterUserDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.GetUserDTO;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Name;
import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Password;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    @Mapping(target = "fullName", expression = "java(domain.getName().fullName())")
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
