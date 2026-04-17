package com.backend.trackinvest.usuarios.domain.rules.wallet;

import com.backend.trackinvest.usuarios.domain.exception.wallet.business.WalletNameInvalidException;

public class NameWalletValidRule {

    public static void validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new WalletNameInvalidException();
        }
        if (name.length() < 3 || name.length() > 25) {
            throw new WalletNameInvalidException();
        }
    }
}
