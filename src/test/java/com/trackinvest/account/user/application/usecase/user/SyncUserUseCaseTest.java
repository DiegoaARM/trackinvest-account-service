package com.trackinvest.account.user.application.usecase.user;

import com.trackinvest.account.user.application.ports.out.UserRepositoryPort;
import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.wallet.domain.models.valueobjects.CurrencyTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private SyncUserUseCase syncUserUseCase;

    @Test
    void shouldCreateUserWhenCognitoIdDoesNotExist() {
        when(userRepository.existsByCognitoId("cognito-123")).thenReturn(false);
        when(userRepository.save(any(UserDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        syncUserUseCase.execute("cognito-123", "email@test.com", "Test User");

        ArgumentCaptor<UserDomain> captor = ArgumentCaptor.forClass(UserDomain.class);
        verify(userRepository).save(captor.capture());
        UserDomain savedUser = captor.getValue();

        assertNotNull(savedUser.getId());
        assertEquals("cognito-123", savedUser.getCognitoId());
        assertEquals("email@test.com", savedUser.getEmail());
        assertEquals("Test User", savedUser.getFullname());
        assertFalse(savedUser.getWalletsList().isEmpty());
        assertEquals("Initial Wallet", savedUser.getWalletsList().getFirst().getName());
        assertEquals(CurrencyTypeEnum.USD, savedUser.getWalletsList().getFirst().getCurrency());
    }

    @Test
    void shouldReturnEarlyWhenCognitoIdAlreadyExists() {
        when(userRepository.existsByCognitoId("existing-cognito")).thenReturn(true);

        syncUserUseCase.execute("existing-cognito", "existing@test.com", "Existing User");

        verify(userRepository, never()).save(any());
    }
}
