package com.backend.trackinvest.usuarios.domain.exception.user.business;

import com.backend.trackinvest.common.exception.DomainException;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException() {
        super(String.format("The user was not found."));
    }
}
