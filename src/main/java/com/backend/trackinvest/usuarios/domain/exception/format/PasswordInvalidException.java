package com.backend.trackinvest.usuarios.domain.exception.format;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class PasswordInvalidException extends DomainException {
    public PasswordInvalidException() {
        super("The password is not hashed: %s");
    }
}
