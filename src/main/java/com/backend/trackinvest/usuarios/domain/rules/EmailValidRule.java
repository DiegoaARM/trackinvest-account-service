package com.backend.trackinvest.usuarios.domain.rules;

import com.backend.trackinvest.usuarios.domain.exception.format.EmailInvalidException;

public final class EmailValidRule implements DomainRule<String>{

    private EmailValidRule() {}

    public static String validate(String data) {
        return new EmailValidRule().check(data);
    }

    public String check(String email) {

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new EmailInvalidException(email);
        }
        return email.trim();
    }
}
