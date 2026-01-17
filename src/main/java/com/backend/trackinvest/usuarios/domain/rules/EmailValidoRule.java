package com.backend.trackinvest.usuarios.domain.rules;

import com.backend.trackinvest.usuarios.domain.exception.format.EmailInvalidoException;

public final class EmailValidoRule implements DomainRule<String>{

    private EmailValidoRule() {}

    public static String validar(String data) {
        return new EmailValidoRule().validate(data);
    }

    public String validate(String email) {

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new EmailInvalidoException(email);
        }
        return email.trim();
    }
}
