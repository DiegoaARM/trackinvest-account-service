package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletMaxNumberException extends DomainException {
    public WalletMaxNumberException() {
        super("A user cannot have more than 10 wallets.");
    }
}
