package com.backend.trackinvest.usuarios.domain.exception.wallet.business;

import com.backend.trackinvest.common.exception.DomainException;

public class WalletNameInvalidException extends DomainException {
    public WalletNameInvalidException() {
        super("Wallet name must be between 3 and 25 characters");
    }
}
