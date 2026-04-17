package com.backend.trackinvest.usuarios.application.ports.in.service.user;

import com.backend.trackinvest.usuarios.application.ports.in.dto.user.GetUserProfileResponseDTO;

public interface GetUserProfilePort {
    GetUserProfileResponseDTO execute(String cognitoId);
}
