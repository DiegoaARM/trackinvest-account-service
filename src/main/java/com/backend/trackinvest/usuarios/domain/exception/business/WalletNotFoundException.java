package com.backend.trackinvest.usuarios.domain.exception.business;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException() {
        super(String.format("The wallet was not found."));
    }
}
