package com.backend.trackinvest.usuarios.domain.exception.format;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class EmailInvalidException extends DomainException {

    public EmailInvalidException(String email) {
        super(String.format("The email format '%s' was not valid.", email));
    }
}
