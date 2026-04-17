package com.backend.trackinvest.usuarios.domain.wallet.exception.business;

import com.backend.trackinvest.common.domain.exception.DomainException;

public class WalletNotFoundException extends DomainException {
    public WalletNotFoundException() {
        super(String.format("The wallet was not found."));
    }
}
