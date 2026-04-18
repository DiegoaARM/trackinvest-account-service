package com.trackinvest.account.usuarios.application.ports.in.service.auth;

import com.trackinvest.account.usuarios.application.ports.in.dto.auth.UrlDTO;

public interface GenerateAuthUrlPort {
    UrlDTO execute();
}
