package com.backend.trackinvest.usuarios.domain.wallet.rules;

import com.backend.trackinvest.usuarios.domain.wallet.exception.format.WalletNameInvalidException;

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
