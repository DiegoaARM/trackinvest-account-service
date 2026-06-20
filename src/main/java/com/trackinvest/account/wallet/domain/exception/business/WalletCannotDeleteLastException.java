package com.trackinvest.account.wallet.domain.exception.business;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class WalletCannotDeleteLastException extends TrackinvestException {
    public WalletCannotDeleteLastException() {
        super("Cannot delete the last wallet of a user");
    }
}
