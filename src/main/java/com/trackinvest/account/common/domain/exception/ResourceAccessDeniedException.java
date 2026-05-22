package com.trackinvest.account.common.domain.exception;

public class ResourceAccessDeniedException extends DomainException {
    public ResourceAccessDeniedException(String resourceName) {
        super(String.format("You do not have permission to access or modify this %s.", resourceName));
    }
}
