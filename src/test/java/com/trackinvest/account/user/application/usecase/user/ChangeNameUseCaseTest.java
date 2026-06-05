package com.trackinvest.account.user.application.usecase.user;

import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.exception.business.UserNotFoundException;
import com.trackinvest.account.user.domain.exception.format.UserNameInvalidException;
import com.trackinvest.account.user.domain.models.UserDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeNameUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private ChangeNameUseCase changeNameUseCase;

    private final UUID userId = UUID.randomUUID();

    @Test
    void shouldChangeNameSuccessfully() {
        UserDomain user = UserDomain.create(userId, "cognito-123", "Old Name", "email@test.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        changeNameUseCase.changeName(userId, "New Name");

        ArgumentCaptor<UserDomain> captor = ArgumentCaptor.forClass(UserDomain.class);
        verify(userRepository).save(captor.capture());
        assertEquals("New Name", captor.getValue().getFullname());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                changeNameUseCase.changeName(userId, "New Name"));

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameIsInvalid() {
        UserDomain user = UserDomain.create(userId, "cognito-123", "Old Name", "email@test.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(UserNameInvalidException.class, () ->
                changeNameUseCase.changeName(userId, "AB"));

        verify(userRepository, never()).save(any());
    }
}
