package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class WalletNotFoundException extends TrackinvestException {
    public WalletNotFoundException() {
        super("The wallet was not found.");
    }
}
