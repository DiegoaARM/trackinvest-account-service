package com.trackinvest.account.user.application.ports.in.service.user;

public interface ChangeNamePort {
    void changeName(String cognitoId, String newFullName);
}
