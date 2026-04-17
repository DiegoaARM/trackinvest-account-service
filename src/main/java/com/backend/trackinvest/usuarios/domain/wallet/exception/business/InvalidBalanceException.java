package com.backend.trackinvest.usuarios.domain.wallet.exception.business;

import com.backend.trackinvest.common.domain.exception.DomainException;

public class InvalidBalanceException extends DomainException {
    public InvalidBalanceException() {
        super("Balance amount must be greater than zero");
    }
}
