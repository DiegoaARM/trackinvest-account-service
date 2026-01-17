package com.backend.trackinvest.usuarios.domain.exception.format;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class EmailInvalidoException extends DomainException {

    public EmailInvalidoException(String email) {
        super(String.format("El formato del email '%s' no es válido.", email));
    }
}
