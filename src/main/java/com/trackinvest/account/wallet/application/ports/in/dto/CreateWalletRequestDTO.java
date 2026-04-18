package com.trackinvest.account.wallet.application.ports.in.dto;

import com.trackinvest.account.wallet.domain.models.valueobjects.CurrencyTypeEnum;

public record CreateWalletRequestDTO(
        String name,
        CurrencyTypeEnum currency
) {
}
