package com.backend.trackinvest.usuarios.application.ports.out;

public interface PasswordEncoderPort {
    /**
     * Transforma la contraseña plana en un hash seguro.
     */
    String encode(String rawPassword);

    /**
     * Verifica si una contraseña plana coincide con un hash.
     */
    boolean matches(String rawPassword, String encodedPassword);
}
