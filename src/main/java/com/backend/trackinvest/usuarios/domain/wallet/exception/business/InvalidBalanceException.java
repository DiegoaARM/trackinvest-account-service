package com.backend.trackinvest.usuarios.domain.wallet.exception.business;

import com.backend.trackinvest.common.exception.DomainException;

public class InvalidBalanceException extends DomainException {
    public InvalidBalanceException() {
        super("Balance amount must be greater than zero");
    }
}
