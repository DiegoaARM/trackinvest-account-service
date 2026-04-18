package com.trackinvest.account.usuarios.domain.wallet.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class InsufficientBalanceException extends DomainException {
    public InsufficientBalanceException() {
        super("Insufficient balance to perform this withdrawal");
    }
}
