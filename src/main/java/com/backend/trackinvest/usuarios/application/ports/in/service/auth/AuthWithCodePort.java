package com.backend.trackinvest.usuarios.application.ports.in.service.auth;

import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.TokenDTO;

public interface AuthWithCodePort {
    TokenDTO execute(String code);
}
