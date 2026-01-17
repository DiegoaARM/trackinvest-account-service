package com.backend.trackinvest.usuarios.domain.models.valueobjects;

import com.backend.trackinvest.usuarios.domain.rules.PasswordValidoRule;

public record Password(String value) {

    public Password {
        PasswordValidoRule.validar(value);
    }
}
