package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletNotFoundException extends DomainException {
    public WalletNotFoundException() {
        super("The wallet was not found.");
    }
}
