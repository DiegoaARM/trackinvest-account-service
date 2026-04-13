package com.backend.trackinvest.usuarios.application.ports.in.dto.user;

import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;

import java.util.UUID;

public record GetUserResponseDTO(
        UUID id,
        String cognitoId,
        String fullName,
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
