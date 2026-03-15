package com.backend.trackinvest.usuarios.domain.models.user.valueobjects;

import com.backend.trackinvest.usuarios.domain.rules.EmailValidRule;

public record Email(String value) {

    public Email {
        value = EmailValidRule.validate(value);
    }
}
