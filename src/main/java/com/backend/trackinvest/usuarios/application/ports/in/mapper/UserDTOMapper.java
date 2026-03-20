package com.backend.trackinvest.usuarios.application.ports.in.mapper;

import com.backend.trackinvest.usuarios.application.ports.in.dto.GetUserDTO;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    @Mapping(target = "fullName", expression = "java(domain.getName().fullName())")
    @Mapping(target = "email", source = "email.value")
    public abstract GetUserDTO toDto(UserDomain domain);

}
