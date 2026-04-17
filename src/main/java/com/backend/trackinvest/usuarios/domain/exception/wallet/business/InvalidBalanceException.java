package com.backend.trackinvest.usuarios.domain.exception.wallet.business;

import com.backend.trackinvest.common.exception.DomainException;

public class InvalidBalanceException extends DomainException {
    public InvalidBalanceException() {
        super("Balance amount must be greater than zero");
    }
}
