package com.trackinvest.account.usuarios.application.ports.in.dto.wallet;

import com.trackinvest.account.usuarios.domain.wallet.models.valueobjects.CurrencyTypeEnum;

public record CreateWalletRequestDTO(
        String name,
        CurrencyTypeEnum currency
) {
}
