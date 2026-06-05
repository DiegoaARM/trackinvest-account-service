package com.trackinvest.account.user.domain.rules;

import com.trackinvest.account.user.domain.exception.format.UserNameInvalidException;

public class UserNameValidRule {

    private static final String NAME_REGEX = "^[a-zA-Z0-9 ]+$";

    private UserNameValidRule() {}

    public static void validate(String name) {
        if (name == null || name.length() < 3 || name.length() > 50) {
            throw new UserNameInvalidException();
        }

        if (!name.matches(NAME_REGEX)) {
            throw new UserNameInvalidException();
        }
    }
}
