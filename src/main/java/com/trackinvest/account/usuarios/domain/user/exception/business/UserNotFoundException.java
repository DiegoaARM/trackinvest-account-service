package com.trackinvest.account.usuarios.domain.user.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException() {
        super(String.format("The user was not found."));
    }
}
