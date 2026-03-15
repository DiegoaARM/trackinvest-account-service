package com.backend.trackinvest.usuarios.domain.exception.business;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

import java.util.UUID;

public class UserIdNotFoundException extends DomainException {

    public UserIdNotFoundException(UUID id) {
        super(String.format("The user with id '%s' was not found.", id));
    }
}
