package com.backend.trackinvest.common.domain.exception;

public class RequiredAttributeException extends DomainException {

    private final String attributeName;

    public RequiredAttributeException(String attributeName) {
        super(String.format("The field '%s' is required.", attributeName));
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
