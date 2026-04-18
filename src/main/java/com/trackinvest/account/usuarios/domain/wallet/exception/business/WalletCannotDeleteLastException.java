package com.trackinvest.account.usuarios.domain.wallet.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

public class WalletCannotDeleteLastException extends DomainException {
    public WalletCannotDeleteLastException() {
        super("Cannot delete the last wallet of a user");
    }
}
