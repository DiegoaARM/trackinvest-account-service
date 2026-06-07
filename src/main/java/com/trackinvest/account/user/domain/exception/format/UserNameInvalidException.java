package com.trackinvest.account.user.domain.exception.format;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class UserNameInvalidException extends TrackinvestException {
    public UserNameInvalidException() {
        super("The user name is invalid. It must be between 3 and 50 characters long and cannot contain special characters.");
    }
}
