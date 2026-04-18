package com.trackinvest.account.user.application.ports.in.dto.auth;

public record AuthResponseDTO(
        String token,
        String type, // Usualmente "Bearer"
        String email,
        String fullName // Para que el frontend diga "Hola, Juan" de inmediato
) {
    // Constructor compacto para asegurar valores por defecto
    public AuthResponseDTO(String token, String email, String fullName) {
        this(token, "Bearer", email, fullName);
    }
}
