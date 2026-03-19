package com.backend.trackinvest.usuarios.infrastructure.adapter.out.security;

import com.backend.trackinvest.usuarios.application.ports.in.dto.CognitoTokenResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.out.IdentityProviderPort;
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
    public String exchangeCodeForToken(String code) {
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
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(response.statusCode() != 200) {
            throw new RuntimeException("Authentication failed. Error: " + response.body());
        }

        CognitoTokenResponseDTO token = MAPPER.readValue(response.body(), CognitoTokenResponseDTO.class);
        return token.id_token();
    }
}
