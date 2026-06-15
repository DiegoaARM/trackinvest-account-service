package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class WalletInsufficientBalanceException extends TrackinvestException {
    public WalletInsufficientBalanceException() {
        super("Insufficient balance to perform this withdrawal");
    }
}
