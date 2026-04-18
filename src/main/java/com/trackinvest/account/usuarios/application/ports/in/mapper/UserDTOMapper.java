package com.trackinvest.account.usuarios.application.ports.in.mapper;

import com.trackinvest.account.usuarios.application.ports.in.dto.user.GetUserResponseDTO;
import com.trackinvest.account.usuarios.domain.user.models.UserDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    GetUserResponseDTO toDto(UserDomain domain);

}
