package com.backend.trackinvest.usuarios.application.ports.in.dto.wallet;

import com.backend.trackinvest.usuarios.domain.models.wallet.valueobjects.CurrencyTypeEnum;

public record CreateWalletRequestDTO(
        String name,
        CurrencyTypeEnum currency
) {
}
