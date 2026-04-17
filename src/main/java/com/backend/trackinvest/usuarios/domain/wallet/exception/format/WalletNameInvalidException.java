package com.backend.trackinvest.usuarios.domain.wallet.exception.format;

import com.backend.trackinvest.common.domain.exception.DomainException;

public class WalletNameInvalidException extends DomainException {
    public WalletNameInvalidException() {
        super("The wallet name is invalid: It must be between 3 and 25 characters long");
    }
}
