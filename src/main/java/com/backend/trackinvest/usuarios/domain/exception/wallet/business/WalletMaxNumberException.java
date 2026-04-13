package com.backend.trackinvest.usuarios.domain.exception.wallet.business;

import com.backend.trackinvest.common.exception.DomainException;

public class WalletMaxNumberException extends DomainException {
    public WalletMaxNumberException(String message) {
        super("A user cannot have more than 10 wallets.");
    }
}
