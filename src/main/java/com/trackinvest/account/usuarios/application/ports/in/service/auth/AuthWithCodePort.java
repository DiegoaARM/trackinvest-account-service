package com.trackinvest.account.usuarios.application.ports.in.service.auth;

import com.trackinvest.account.usuarios.application.ports.in.dto.auth.TokenDTO;

public interface AuthWithCodePort {
    TokenDTO execute(String code);
}
