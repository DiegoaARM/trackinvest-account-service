package com.trackinvest.account.usuarios.domain.wallet.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletUnauthorizedException extends DomainException {
    public WalletUnauthorizedException() {
        super("You are not authorized to perform this action on this wallet");
    }
}
