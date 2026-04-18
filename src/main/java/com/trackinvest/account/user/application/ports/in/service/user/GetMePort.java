package com.trackinvest.account.user.application.ports.in.service.user;

import com.trackinvest.account.user.application.ports.in.dto.user.GetUserResponseDTO;

public interface GetMePort {
    GetUserResponseDTO execute(String cognitoId);
}
