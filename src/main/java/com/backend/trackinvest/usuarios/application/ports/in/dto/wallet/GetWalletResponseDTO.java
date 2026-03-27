package com.backend.trackinvest.usuarios.application.ports.in.dto.wallet;

import com.backend.trackinvest.usuarios.domain.models.wallet.WalletDomain;
import com.backend.trackinvest.usuarios.domain.models.wallet.valueobjects.CurrencyTypeEnum;

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
