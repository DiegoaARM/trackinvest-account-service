package com.backend.trackinvest.usuarios.domain.exception.wallet.business;

import com.backend.trackinvest.common.exception.DomainException;

public class WalletCannotDeleteLastException extends DomainException {
    public WalletCannotDeleteLastException() {
        super("Cannot delete the last wallet of a user");
    }
}
