package com.trackinvest.account.user.application.ports.in.dto.user;

import com.trackinvest.account.user.domain.models.UserDomain;

import java.util.UUID;

public record GetUserResponseDTO(
        UUID id,
        String cognitoId,
        String fullname,
        String email) {

    public static GetUserResponseDTO fromDomain(UserDomain user) {
        return new GetUserResponseDTO(
                user.getId(),
                user.getCognitoId(),
                user.getEmail(),
                user.getFullname()
        );
    }
}
