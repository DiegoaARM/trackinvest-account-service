package com.trackinvest.account.wallet.domain.exception.format;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class WalletAmountInvalidException extends TrackinvestException {
    public WalletAmountInvalidException() {
        super("Balance amount must be greater than zero");
    }
}
