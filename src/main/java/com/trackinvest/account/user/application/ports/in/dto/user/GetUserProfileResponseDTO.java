package com.trackinvest.account.user.application.ports.in.dto.user;

import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.user.domain.models.UserDomain;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record GetUserProfileResponseDTO(
        UUID id,
        String cognitoId,
        String fullName,
        String email,
        List<GetWalletResponseDTO> wallets
) {

    public static GetUserProfileResponseDTO fromDomain(UserDomain user) {
        return new GetUserProfileResponseDTO(
                user.getId(),
                user.getCognitoId(),
                user.getFullname(),
                user.getEmail(),
                user.getWalletsList().stream()
                        .map(GetWalletResponseDTO::fromDomain)
                        .collect(Collectors.toList())
        );
    }
}
