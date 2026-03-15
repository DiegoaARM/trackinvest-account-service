package com.backend.trackinvest.usuarios.domain.models.user.valueobjects;

import com.backend.trackinvest.usuarios.domain.rules.PasswordValidRule;

public record Password(String value) {

    public Password {
        PasswordValidRule.validate(value);
    }
}
