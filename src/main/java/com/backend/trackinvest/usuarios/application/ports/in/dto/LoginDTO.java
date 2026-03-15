package com.backend.trackinvest.usuarios.application.ports.in.dto;

public record LoginDTO(
        String email,
        String password) {
}
