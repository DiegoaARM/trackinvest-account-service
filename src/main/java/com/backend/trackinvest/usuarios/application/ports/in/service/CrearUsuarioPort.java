package com.backend.trackinvest.usuarios.application.ports.in.service;

import com.backend.trackinvest.usuarios.application.ports.in.dto.CrearUsuarioDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.ObtenerUsuarioDTO;
import com.backend.trackinvest.usuarios.domain.models.UsuarioDomain;

public interface CrearUsuarioPort {
    ObtenerUsuarioDTO execute(CrearUsuarioDTO dto);
}
