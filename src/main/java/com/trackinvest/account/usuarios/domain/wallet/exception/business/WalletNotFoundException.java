package com.trackinvest.account.usuarios.domain.wallet.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletNotFoundException extends DomainException {
    public WalletNotFoundException() {
        super(String.format("The wallet was not found."));
    }
}
