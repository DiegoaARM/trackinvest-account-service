package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.common.domain.exception.ResourceAccessDeniedException;
import com.trackinvest.account.common.domain.service.AuthorizationService;
import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletCannotDeleteLastException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.models.valueobjects.CurrencyTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteWalletUseCaseTest {

    @Mock
    private WalletRepositoryPort walletRepository;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private DeleteWalletUseCase deleteWalletUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID walletId = UUID.randomUUID();

    @Test
    void shouldDeleteWalletSuccessfully() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, wallet.getUser().getId(), "wallet");
        when(walletRepository.countByUserId(userId)).thenReturn(2L);

        deleteWalletUseCase.execute(userId, walletId);

        verify(walletRepository).delete(walletId);
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () ->
                deleteWalletUseCase.execute(userId, walletId));

        verify(walletRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        UserDomain otherUser = UserDomain.create(UUID.randomUUID());
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", otherUser, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doThrow(new ResourceAccessDeniedException("wallet"))
                .when(authorizationService).verifyOwner(userId, otherUser.getId(), "wallet");

        assertThrows(ResourceAccessDeniedException.class, () ->
                deleteWalletUseCase.execute(userId, walletId));

        verify(walletRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingLastWallet() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, wallet.getUser().getId(), "wallet");
        when(walletRepository.countByUserId(userId)).thenReturn(1L);

        assertThrows(WalletCannotDeleteLastException.class, () ->
                deleteWalletUseCase.execute(userId, walletId));

        verify(walletRepository, never()).delete(any());
    }
}
