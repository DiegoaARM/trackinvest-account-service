package com.backend.trackinvest.usuarios.application.ports.in.dto;

import java.util.UUID;

public record ObtenerUsuarioDTO(
        UUID id,
        String nombreCompleto,
        String email) {
}
