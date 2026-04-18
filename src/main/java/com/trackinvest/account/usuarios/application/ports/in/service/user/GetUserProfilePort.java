package com.trackinvest.account.usuarios.application.ports.in.service.user;

import com.trackinvest.account.usuarios.application.ports.in.dto.user.GetUserProfileResponseDTO;

public interface GetUserProfilePort {
    GetUserProfileResponseDTO execute(String cognitoId);
}
