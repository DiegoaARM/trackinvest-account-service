package com.trackinvest.account.usuarios.application.ports.in.dto.wallet;

import com.trackinvest.account.usuarios.domain.wallet.models.WalletDomain;
import com.trackinvest.account.usuarios.domain.wallet.models.valueobjects.CurrencyTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetWalletResponseDTO(
        UUID id,
        String name,
        BigDecimal balance,
        CurrencyTypeEnum currency,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static GetWalletResponseDTO fromDomain(WalletDomain domain) {
        return new GetWalletResponseDTO(
                domain.getId(),
                domain.getName(),
                domain.getBalance(),
                domain.getCurrency(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
