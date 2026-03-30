package com.backend.trackinvest.usuarios.domain.exception.format;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class UserEmailInvalidException extends DomainException {

    public UserEmailInvalidException(String email) {
        super(String.format("The email format '%s' was not valid.", email));
    }
}
