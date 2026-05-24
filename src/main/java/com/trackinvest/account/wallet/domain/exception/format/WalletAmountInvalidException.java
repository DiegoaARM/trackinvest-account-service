package com.trackinvest.account.wallet.domain.exception.format;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletAmountInvalidException extends DomainException {
    public WalletAmountInvalidException() {
        super("Balance amount must be greater than zero");
    }
}
