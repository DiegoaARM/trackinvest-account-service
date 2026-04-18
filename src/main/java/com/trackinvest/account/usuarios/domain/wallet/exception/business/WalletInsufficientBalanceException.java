package com.trackinvest.account.usuarios.domain.wallet.exception.business;

import com.trackinvest.account.common.domain.exception.DomainException;

import java.math.BigDecimal;

public class WalletInsufficientBalanceException extends DomainException {
    public WalletInsufficientBalanceException(BigDecimal balance) {
        super(String.format("The wallet balance cannot be less than 1. Actual balance: %s", balance));
    }
}
