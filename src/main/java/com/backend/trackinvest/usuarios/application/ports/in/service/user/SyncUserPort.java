package com.backend.trackinvest.usuarios.application.ports.in.service.user;

public interface SyncUserPort {
    void execute(String cognitoId, String email, String fullname);
}
