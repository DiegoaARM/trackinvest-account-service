package com.backend.trackinvest.usuarios.application.ports.in.service;

import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.TokenDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.UrlDTO;

public interface AuthPort {
    UrlDTO getAuthUrl();
    TokenDTO authenticateWithCode(String code);
}
