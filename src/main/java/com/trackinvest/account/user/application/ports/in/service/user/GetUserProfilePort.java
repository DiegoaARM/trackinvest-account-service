package com.trackinvest.account.user.application.ports.in.service.user;

import com.trackinvest.account.user.application.ports.in.dto.user.GetUserProfileResponseDTO;

public interface GetUserProfilePort {
    GetUserProfileResponseDTO execute(String cognitoId);
}
