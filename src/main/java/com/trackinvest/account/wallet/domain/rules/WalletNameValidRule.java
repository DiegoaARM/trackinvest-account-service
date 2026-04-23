package com.trackinvest.account.wallet.domain.rules;

import com.trackinvest.account.wallet.domain.exception.format.WalletNameInvalidException;

public class WalletNameValidRule {

    public static void validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new WalletNameInvalidException();
        }
        if (name.length() < 3 || name.length() > 25) {
            throw new WalletNameInvalidException();
        }
    }
}
