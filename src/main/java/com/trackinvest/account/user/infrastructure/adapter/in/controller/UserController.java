package com.trackinvest.account.user.infrastructure.adapter.in.controller;

import com.trackinvest.account.user.application.ports.in.dto.user.GetUserResponseDTO;
import com.trackinvest.account.user.application.ports.in.dto.user.GetUserProfileResponseDTO;
import com.trackinvest.account.user.application.ports.in.service.user.GetMePort;
import com.trackinvest.account.user.application.ports.in.service.user.GetUserProfilePort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for profile management and user information")
public class UserController {

    private final GetMePort getMePort;
    private final GetUserProfilePort getUserProfilePort;

    @GetMapping("/me")
    public ResponseEntity<GetUserResponseDTO> getMe(@AuthenticationPrincipal Jwt jwt) {
        String cognitoId = jwt.getSubject();
        GetUserResponseDTO userDTO = getMePort.execute(cognitoId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<GetUserProfileResponseDTO> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
        String cognitoId = jwt.getSubject();
        GetUserProfileResponseDTO profileDTO = getUserProfilePort.execute(cognitoId);
        return ResponseEntity.ok(profileDTO);
    }
}
