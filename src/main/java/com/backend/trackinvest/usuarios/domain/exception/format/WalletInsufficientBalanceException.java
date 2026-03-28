package com.backend.trackinvest.usuarios.domain.exception.format;

import com.backend.trackinvest.usuarios.domain.exception.DomainException;

import java.math.BigDecimal;

public class WalletInsufficientBalanceException extends DomainException {
    public WalletInsufficientBalanceException(BigDecimal balance) {
        super(String.format("The wallet balance cannot be less than 1. Actual balance: %s", balance));
    }
}
