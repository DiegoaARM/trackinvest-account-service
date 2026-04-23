package com.trackinvest.account.user.application.ports.in.service.auth;

import com.trackinvest.account.user.application.ports.in.dto.auth.TokenDTO;

public interface AuthWithCodePort {
    TokenDTO execute(String code);
}
