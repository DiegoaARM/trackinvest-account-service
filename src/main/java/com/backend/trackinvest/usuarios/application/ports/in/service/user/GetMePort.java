package com.backend.trackinvest.usuarios.application.ports.in.service.user;

import com.backend.trackinvest.usuarios.application.ports.in.dto.user.GetUserResponseDTO;

public interface GetMePort {
    GetUserResponseDTO execute(String cognitoId);
}
