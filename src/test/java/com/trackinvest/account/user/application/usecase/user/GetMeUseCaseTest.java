package com.trackinvest.account.user.application.usecase.user;

import com.trackinvest.account.user.application.ports.in.dto.user.GetUserResponseDTO;
import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.exception.business.UserNotFoundException;
import com.trackinvest.account.user.domain.models.UserDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetMeUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private GetMeUseCase getMeUseCase;

    private final UUID userId = UUID.randomUUID();

    @Test
    void shouldReturnUserDTOWhenUserFound() {
        UserDomain user = UserDomain.create(userId, "cognito-123", "Test User", "test@email.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        GetUserResponseDTO response = getMeUseCase.execute(userId);

        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("cognito-123", response.cognitoId());
        assertEquals("Test User", response.fullname());
        assertEquals("test@email.com", response.email());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                getMeUseCase.execute(userId));
    }
}
