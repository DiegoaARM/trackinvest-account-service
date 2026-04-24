package com.trackinvest.account.user.domain.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException() {
        super(String.format("The user was not found."));
    }
}
