package com.backend.trackinvest.usuarios.domain.exception.format;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

public class NombreInvalidoException extends DomainException {
    public NombreInvalidoException(String nombre) {
        super(String.format("El nombre '%s' es inválido: debe tener entre 3 y 25 caracteres.", nombre));
    }
}
