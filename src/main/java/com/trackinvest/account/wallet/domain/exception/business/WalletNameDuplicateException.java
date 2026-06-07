package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class WalletNameDuplicateException extends TrackinvestException {
    public WalletNameDuplicateException() {
        super("Wallet name already exists for this user");
    }
}
