package com.backend.trackinvest.usuarios.application.ports.in.service.user;

import com.backend.trackinvest.usuarios.application.ports.in.dto.user.GetUserResponseDTO;
import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;

public interface GetMePort {
    GetUserResponseDTO execute(String cognitoId);
}
