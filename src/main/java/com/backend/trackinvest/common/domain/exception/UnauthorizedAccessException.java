package com.backend.trackinvest.common.domain.exception;

public class UnauthorizedAccessException extends DomainException {
    public UnauthorizedAccessException() {
        super("No tienes permisos para realizar esta acción sobre este recurso.");
    }
}
