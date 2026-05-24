package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletInsufficientBalanceException extends DomainException {
    public WalletInsufficientBalanceException() {
        super("Insufficient balance to perform this withdrawal");
    }
}
