package com.trackinvest.account.common.domain.exception;

public class RequiredAttributeException extends TrackinvestException {

    public RequiredAttributeException(String attributeName) {
        super(String.format("The field '%s' is required.", attributeName));
    }
}
