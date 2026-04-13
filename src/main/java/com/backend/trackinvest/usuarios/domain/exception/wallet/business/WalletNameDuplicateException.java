package com.backend.trackinvest.usuarios.domain.exception.wallet.business;

import com.backend.trackinvest.common.exception.DomainException;

public class WalletNameDuplicateException extends DomainException {
    protected WalletNameDuplicateException(String message) {
        super(message);
    }
}
