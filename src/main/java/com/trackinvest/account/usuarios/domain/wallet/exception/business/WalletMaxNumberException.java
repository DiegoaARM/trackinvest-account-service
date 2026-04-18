package com.trackinvest.account.usuarios.domain.wallet.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletMaxNumberException extends DomainException {
    public WalletMaxNumberException(String message) {
        super("A user cannot have more than 10 wallets.");
    }
}
