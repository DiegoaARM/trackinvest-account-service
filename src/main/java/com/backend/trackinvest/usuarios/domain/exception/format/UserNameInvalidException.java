package com.backend.trackinvest.usuarios.domain.exception.format;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class UserNameInvalidException extends DomainException {
    public UserNameInvalidException(String name) {
        super(String.format("The name '%s' is invalid: It must be between 3 and 25 characters long..", name));
    }
}
