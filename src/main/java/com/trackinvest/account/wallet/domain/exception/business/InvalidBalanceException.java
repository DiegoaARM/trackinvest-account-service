package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class InvalidBalanceException extends DomainException {
    public InvalidBalanceException() {
        super("Balance amount must be greater than zero");
    }
}
