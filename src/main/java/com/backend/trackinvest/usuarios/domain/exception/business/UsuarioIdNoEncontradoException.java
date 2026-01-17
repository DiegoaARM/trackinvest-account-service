package com.backend.trackinvest.usuarios.domain.exception.business;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

import java.util.UUID;

public class UsuarioIdNoEncontradoException extends DomainException {

    public UsuarioIdNoEncontradoException(UUID id) {
        super(String.format("El Usuario con el id '%s' no fue encontrado.", id));
    }
}
