package com.trackinvest.account.user.application.ports.in.mapper;

import com.trackinvest.account.user.application.ports.in.dto.user.GetUserResponseDTO;
import com.trackinvest.account.user.domain.models.UserDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    GetUserResponseDTO toDto(UserDomain domain);

}
