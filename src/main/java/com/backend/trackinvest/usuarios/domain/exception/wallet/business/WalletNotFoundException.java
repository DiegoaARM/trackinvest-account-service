package com.backend.trackinvest.usuarios.domain.exception.wallet.business;

import com.backend.trackinvest.common.exception.DomainException;

public class WalletNotFoundException extends DomainException {
    public WalletNotFoundException() {
        super(String.format("The wallet was not found."));
    }
}
