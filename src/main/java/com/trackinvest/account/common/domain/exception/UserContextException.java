package com.trackinvest.account.common.domain.exception;

public class UserContextException extends TrackinvestException {

    public UserContextException(String cognitoId) {
        super(String.format("No account was found for the authenticated principal '%s'.", cognitoId));
    }

}
