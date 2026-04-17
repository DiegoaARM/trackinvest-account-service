package com.backend.trackinvest.usuarios.domain.wallet.exception.business;

import com.backend.trackinvest.common.domain.exception.DomainException;

public class WalletCannotDeleteLastException extends DomainException {
    public WalletCannotDeleteLastException() {
        super("Cannot delete the last wallet of a user");
    }
}
