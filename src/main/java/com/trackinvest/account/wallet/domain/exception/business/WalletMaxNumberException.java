package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class WalletMaxNumberException extends TrackinvestException {
    public WalletMaxNumberException() {
        super("A user cannot have more than 10 wallets.");
    }
}
