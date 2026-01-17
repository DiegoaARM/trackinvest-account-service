package com.backend.trackinvest.usuarios.domain.exception.business;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class EmailNoEncontradoException extends DomainException {

    public EmailNoEncontradoException(String email) {
        super(String.format("El email '%s' no fue encontrado.", email));
    }
}
