package com.trackinvest.account.usuarios.application.ports.in.service.user;

public interface ChangeNamePort {
    void changeName(String cognitoId, String newFullName);
}
