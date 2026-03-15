package com.backend.trackinvest.usuarios.domain.rules;

import com.backend.trackinvest.usuarios.domain.exception.format.PasswordInvalidException;

public final class PasswordValidRule implements DomainRule<String> {

    private PasswordValidRule() {}

    public static String validate(String data) {
        return new PasswordValidRule().check(data);
    }

    public String check(String password) {

        if (password == null || password.isBlank()) {
            throw new PasswordInvalidException();
        }

        if (password.length() < 30) {
            throw new PasswordInvalidException();
        }

        return password;
    }
}
