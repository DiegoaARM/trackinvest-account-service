package com.backend.trackinvest.usuarios.domain.exception.wallet.business;

import com.backend.trackinvest.common.exception.DomainException;

import java.math.BigDecimal;

public class WalletInsufficientBalanceException extends DomainException {
    public WalletInsufficientBalanceException(BigDecimal balance) {
        super(String.format("The wallet balance cannot be less than 1. Actual balance: %s", balance));
    }
}
