package com.backend.trackinvest.usuarios.application.ports.in.service.auth;

import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.UrlDTO;

public interface GenerateAuthUrlPort {
    UrlDTO execute();
}
