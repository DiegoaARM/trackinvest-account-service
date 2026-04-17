package com.backend.trackinvest.usuarios.application.ports.in.dto.wallet;

import java.math.BigDecimal;

public record UpdateWalletBalanceRequestDTO(
        BigDecimal amount,
        boolean isDeposit
) {
}
