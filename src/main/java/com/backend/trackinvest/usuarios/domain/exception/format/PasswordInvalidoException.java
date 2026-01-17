package com.backend.trackinvest.usuarios.domain.exception.format;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class PasswordInvalidoException extends DomainException {
    public PasswordInvalidoException() {
        super("La contraseña es inválida: %s");
    }
}
