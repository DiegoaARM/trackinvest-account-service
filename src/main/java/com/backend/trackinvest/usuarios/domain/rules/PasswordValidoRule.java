package com.backend.trackinvest.usuarios.domain.rules;

import com.backend.trackinvest.usuarios.domain.exception.format.PasswordInvalidoException;

public final class PasswordValidoRule implements DomainRule<String> {

    private PasswordValidoRule() {}

    public static String validar(String data) {
        return new PasswordValidoRule().validate(data);
    }

    public String validate(String password) {

        if (password == null || password.isBlank()) {
            throw new PasswordInvalidoException();
        }
        // Validamos que sea un hash (por ejemplo, longitud mínima de un hash de BCrypt es 60)
        if (password.length() < 30) {
            throw new PasswordInvalidoException();
        }

        return password;
    }
}
