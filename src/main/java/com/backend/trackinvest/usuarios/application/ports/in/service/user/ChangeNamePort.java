package com.backend.trackinvest.usuarios.application.ports.in.service.user;

public interface ChangeNamePort {
    void changeName(String cognitoId, String newFullName);
}
