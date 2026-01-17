package com.backend.trackinvest.usuarios.application.ports.in.dto;

public record CrearUsuarioDTO(
        String nombres,
        String apellidos,
        String email,
        String password) {
}
