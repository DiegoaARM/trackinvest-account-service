package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletCannotDeleteLastException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteWalletUseCaseTest {

    @Mock
    private WalletRepositoryPort walletRepository;

    @InjectMocks
    private DeleteWalletUseCase deleteWalletUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID walletId = UUID.randomUUID();

    @Test
    void shouldDeleteWalletSuccessfully() {
        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);
        when(walletRepository.countByUserId(userId)).thenReturn(2L);

        deleteWalletUseCase.execute(userId, walletId);

        verify(walletRepository).delete(walletId);
    }

    @Test
    void shouldThrowExceptionWhenWalletIsNotFoundOrNotOwnedByUser() {
        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(false);

        assertThrows(WalletNotFoundException.class, () ->
                deleteWalletUseCase.execute(userId, walletId));

        verify(walletRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingLastWallet() {
        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);
        when(walletRepository.countByUserId(userId)).thenReturn(1L);

        assertThrows(WalletCannotDeleteLastException.class, () ->
                deleteWalletUseCase.execute(userId, walletId));

        verify(walletRepository, never()).delete(any());
    }
}
