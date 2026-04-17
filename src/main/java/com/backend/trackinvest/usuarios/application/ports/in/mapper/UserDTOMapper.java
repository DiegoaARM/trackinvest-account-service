package com.backend.trackinvest.usuarios.application.ports.in.mapper;

import com.backend.trackinvest.usuarios.application.ports.in.dto.user.GetUserResponseDTO;
import com.backend.trackinvest.usuarios.domain.user.models.UserDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    GetUserResponseDTO toDto(UserDomain domain);

}
