package com.trackinvest.account.user.application.ports.in.service.user;

public interface SyncUserPort {
    void execute(String cognitoId, String email, String fullname);
}
