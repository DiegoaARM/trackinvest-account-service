package com.backend.trackinvest.usuarios.domain.wallet.exception.business;

import com.backend.trackinvest.common.domain.exception.DomainException;

public class WalletUnauthorizedException extends DomainException {
    public WalletUnauthorizedException() {
        super("You are not authorized to perform this action on this wallet");
    }
}
