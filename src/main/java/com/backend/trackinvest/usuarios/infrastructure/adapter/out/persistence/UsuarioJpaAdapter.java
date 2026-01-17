package com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence;

import com.backend.trackinvest.usuarios.application.ports.out.UsuarioRepositoryPort;
import com.backend.trackinvest.usuarios.domain.models.UsuarioDomain;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.mapper.UsuarioEntityMapper;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioJpaAdapter implements UsuarioRepositoryPort {


    private final UsuarioRepository usuarioRepository;
    private final UsuarioEntityMapper usuarioEntityMapper;

    public UsuarioJpaAdapter(UsuarioRepository usuarioRepository, UsuarioEntityMapper usuarioEntityMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioEntityMapper = usuarioEntityMapper;
    }

    @Override
    public Optional<UsuarioDomain> findById(UUID id) {
        return usuarioRepository.findById(id)
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    public Optional<UsuarioDomain> findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    public UsuarioDomain save(UsuarioDomain usuario) {
        return usuarioEntityMapper.toDomain(
                usuarioRepository.save(
                        usuarioEntityMapper.toEntity(usuario)
                )
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
