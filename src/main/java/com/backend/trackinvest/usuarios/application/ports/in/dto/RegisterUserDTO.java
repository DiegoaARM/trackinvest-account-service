package com.backend.trackinvest.usuarios.application.ports.in.dto;

public record RegisterUserDTO(
        String firstName,
        String lastName,
        String email,
        String password) {
}
