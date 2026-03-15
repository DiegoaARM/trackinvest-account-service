package com.backend.trackinvest.usuarios.application.ports.in.dto;

import java.util.UUID;

public record GetUserDTO(
        UUID id,
        String fullName,
        String email) {
}
