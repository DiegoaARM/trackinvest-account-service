package com.backend.trackinvest.usuarios.domain.exception.business;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

import java.util.UUID;

public class CognitoIdNotFoundException extends DomainException {
    public CognitoIdNotFoundException(String cognitoId) {
        super(String.format("The user with id '%s' was not found.", cognitoId));
    }
}
