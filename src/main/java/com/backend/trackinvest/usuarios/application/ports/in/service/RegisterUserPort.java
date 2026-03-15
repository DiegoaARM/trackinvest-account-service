package com.backend.trackinvest.usuarios.application.ports.in.service;

import com.backend.trackinvest.usuarios.application.ports.in.dto.RegisterUserDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.GetUserDTO;

public interface RegisterUserPort {
    GetUserDTO execute(RegisterUserDTO dto);
}
