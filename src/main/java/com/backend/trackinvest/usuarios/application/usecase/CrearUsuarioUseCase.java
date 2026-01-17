package com.backend.trackinvest.usuarios.application.usecase;

import com.backend.trackinvest.usuarios.application.ports.in.dto.CrearUsuarioDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.ObtenerUsuarioDTO;
import com.backend.trackinvest.usuarios.application.ports.in.mapper.UsuarioDTOMapper;
import com.backend.trackinvest.usuarios.application.ports.in.service.CrearUsuarioPort;
import com.backend.trackinvest.usuarios.application.ports.out.PasswordEncoderPort;
import com.backend.trackinvest.usuarios.application.ports.out.UsuarioRepositoryPort;
import com.backend.trackinvest.usuarios.domain.models.UsuarioDomain;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Email;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Nombre;
import com.backend.trackinvest.usuarios.domain.models.valueobjects.Password;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrearUsuarioUseCase implements CrearUsuarioPort {

    private final UsuarioRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final UsuarioDTOMapper usuarioDTOMapper;

    @Override
    @Transactional
    public ObtenerUsuarioDTO execute(CrearUsuarioDTO dto) {

        // 1. Validamos los datos básicos (Aquí saltará tu NombreInvalidoException)
        // Pero no lo guardamos en una variable 'usuario' final todavía.
        Nombre nombre = usuarioDTOMapper.mapNombre(dto);
        Email email = new Email(dto.email());

        // 2. Preparamos los datos que el DTO no tiene
        UUID id = UUID.randomUUID();
        String passHasheada = passwordEncoder.encode(dto.password());
        Password password = new Password(passHasheada);

        // 3. CREACIÓN ÚNICA: El objeto nace con TODO lo necesario.
        // Sin setters. Sin estados intermedios inválidos.
        UsuarioDomain usuario = UsuarioDomain.create(
                id,
                nombre,
                email,
                password
        );

        // 4. Persistir y responder
        UsuarioDomain usuarioGuardado = userRepository.save(usuario);
        return usuarioDTOMapper.toDto(usuarioGuardado);
    }
}
