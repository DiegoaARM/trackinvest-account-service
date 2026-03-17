package com.backend.trackinvest.usuarios.application.ports.in.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CognitoTokenResponseDTO(String id_token) {
}
