package com.backend.trackinvest.usuarios.domain.models.valueobjects;

import com.backend.trackinvest.usuarios.domain.rules.EmailValidoRule;

import java.util.regex.Pattern;

public record Email(String value) {

    public Email {
        // La regla valida el String y devuelve el valor limpio
        value = EmailValidoRule.validar(value);
    }
}
