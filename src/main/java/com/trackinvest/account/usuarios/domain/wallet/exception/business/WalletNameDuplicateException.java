package com.trackinvest.account.usuarios.domain.wallet.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletNameDuplicateException extends DomainException {
    public WalletNameDuplicateException() {
        super("Wallet name already exists for this user");
    }
}
