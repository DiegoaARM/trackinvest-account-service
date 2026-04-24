package com.trackinvest.account.wallet.application.ports.in.service;

public interface DeleteWalletPort {
    void execute(String cognitoId, java.util.UUID walletId);
}
