package com.trackinvest.account.user.application.ports.in.service.auth;

import com.trackinvest.account.user.application.ports.in.dto.auth.UrlDTO;

public interface GenerateAuthUrlPort {
    UrlDTO execute();
}
