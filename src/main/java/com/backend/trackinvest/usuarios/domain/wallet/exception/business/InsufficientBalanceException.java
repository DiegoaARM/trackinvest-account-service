package com.backend.trackinvest.usuarios.domain.wallet.exception.business;

import com.backend.trackinvest.common.exception.DomainException;

public class InsufficientBalanceException extends DomainException {
    public InsufficientBalanceException() {
        super("Insufficient balance to perform this withdrawal");
    }
}
