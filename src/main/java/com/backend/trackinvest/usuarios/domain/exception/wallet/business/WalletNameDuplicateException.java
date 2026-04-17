package com.backend.trackinvest.usuarios.domain.exception.wallet.business;

import com.backend.trackinvest.common.exception.DomainException;

public class WalletNameDuplicateException extends DomainException {
    public WalletNameDuplicateException() {
        super("Wallet name already exists for this user");
    }
}
