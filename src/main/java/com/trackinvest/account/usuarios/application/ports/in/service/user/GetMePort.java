package com.trackinvest.account.usuarios.application.ports.in.service.user;

import com.trackinvest.account.usuarios.application.ports.in.dto.user.GetUserResponseDTO;

public interface GetMePort {
    GetUserResponseDTO execute(String cognitoId);
}
