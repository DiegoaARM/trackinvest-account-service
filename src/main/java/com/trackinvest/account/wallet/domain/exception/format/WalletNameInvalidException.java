package com.trackinvest.account.wallet.domain.exception.format;

import com.trackinvest.account.common.domain.exception.TrackinvestException;

public class WalletNameInvalidException extends TrackinvestException {
    public WalletNameInvalidException() {
        super("The wallet name is invalid: It must be between 3 and 25 characters long");
    }
}
