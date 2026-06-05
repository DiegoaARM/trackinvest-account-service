package com.trackinvest.account.user.infrastructure.adapter.out.security;

import com.trackinvest.account.user.application.ports.in.dto.auth.TokenDTO;
import com.trackinvest.account.user.application.ports.out.IdentityProviderPort;
import com.trackinvest.account.user.domain.exception.business.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Component
public class CognitoAuthAdapter implements IdentityProviderPort {

    @Value("${auth.cognitoUri}")
    private String cognitoUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.jwt.clientSecret}")
    private String clientSecret;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String generateAuthorizationUrl() {
        return cognitoUri +
                "/oauth2/authorize?" +
                "response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=http://localhost:8081/oauth2/idpresponse" +
                "&scope=email+openid+profile";
    }

    @Override
    public TokenDTO exchangeCodeForToken(String code) {
        String urlStr = cognitoUri +
                "/oauth2/token?" +
                "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&code=" + code +
                "&redirect_uri=http://localhost:8081/oauth2/idpresponse";
        String authInfo = clientId + ":" + clientSecret;
        String basicAuth = Base64.getEncoder().encodeToString(authInfo.getBytes());

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(new URI(urlStr))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + basicAuth)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
        } catch (URISyntaxException e) {
            throw new AuthenticationException("Error al estructurar la petición de autenticación interna", e);
        }

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new AuthenticationException("Error de comunicación de red con el proveedor de identidad", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Buena práctica obligatoria en Java al capturar InterruptedException
            throw new AuthenticationException("La solicitud de autenticación fue interrumpida", e);
        }

        if(response.statusCode() != 200) {
            throw new AuthenticationException("No se pudo validar el código de autorización. Proveedor respondió: " + response.statusCode());
        }

        return MAPPER.readValue(response.body(), TokenDTO.class);
    }

    @Override
    public TokenDTO refreshTokens(String refreshToken) {
        String urlStr = cognitoUri +
                "/oauth2/token?" +
                "grant_type=refresh_token" +
                "&client_id=" + clientId +
                "&refresh_token=" + refreshToken;

        String authInfo = clientId + ":" + clientSecret;
        String basicAuth = Base64.getEncoder().encodeToString(authInfo.getBytes());

        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(urlStr))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + basicAuth)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response;
            try (HttpClient client = HttpClient.newHttpClient()) {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            if (response.statusCode() != 200) {
                throw new AuthenticationException("No se pudo renovar la sesión del usuario. Estado: " + response.statusCode());
            }

            return MAPPER.readValue(response.body(), TokenDTO.class);

        } catch (URISyntaxException | IOException e) {
            throw new AuthenticationException("Error de infraestructura durante la renovación del token", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AuthenticationException("La renovación del token fue interrumpida", e);
        }
    }
}