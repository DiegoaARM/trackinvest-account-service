package com.trackinvest.account.wallet.application.ports.in.dto;

import java.math.BigDecimal;

public record UpdateWalletBalanceRequestDTO(
        BigDecimal amount,
        boolean isDeposit
) {
}
