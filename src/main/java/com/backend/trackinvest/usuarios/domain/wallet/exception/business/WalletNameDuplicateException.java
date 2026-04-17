package com.backend.trackinvest.usuarios.domain.wallet.exception.business;

import com.backend.trackinvest.common.domain.exception.DomainException;

public class WalletNameDuplicateException extends DomainException {
    public WalletNameDuplicateException() {
        super("Wallet name already exists for this user");
    }
}
