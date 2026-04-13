package com.backend.trackinvest.usuarios.application.ports.in.mapper;

import com.backend.trackinvest.usuarios.application.ports.in.dto.user.GetUserResponseDTO;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    GetUserResponseDTO toDto(UserDomain domain);

}
