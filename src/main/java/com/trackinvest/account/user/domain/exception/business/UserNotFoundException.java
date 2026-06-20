package com.trackinvest.account.user.domain.exception.business;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class UserNotFoundException extends TrackinvestException {

    public UserNotFoundException() {
        super("The user was not found.");
    }
}
