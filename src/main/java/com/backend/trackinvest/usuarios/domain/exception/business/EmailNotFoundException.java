package com.backend.trackinvest.usuarios.domain.exception.business;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class EmailNotFoundException extends DomainException {

    public EmailNotFoundException(String email) {
        super(String.format("The email '%s' was not found.", email));
    }
}
