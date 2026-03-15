package com.backend.trackinvest.usuarios.application.ports.in.service;

import com.backend.trackinvest.usuarios.application.ports.in.dto.LoginDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.AuthResponseDTO;

public interface AutenticateUserPort {
    AuthResponseDTO execute(LoginDTO dto);
}
