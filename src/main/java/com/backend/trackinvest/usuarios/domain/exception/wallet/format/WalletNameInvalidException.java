package com.backend.trackinvest.usuarios.domain.exception.wallet.format;

import com.backend.trackinvest.common.exception.DomainException;

public class WalletNameInvalidException extends DomainException {
    public WalletNameInvalidException(String name) {
        super(String.format("The wallet name '%s' is invalid: It must be between 3 and 25 characters long..", name));
    }
}
