package com.trackinvest.account.usuarios.application.ports.in.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CognitoTokenResponseDTO(String id_token) {
}
