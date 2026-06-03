package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.common.domain.exception.ResourceAccessDeniedException;
import com.trackinvest.account.common.domain.service.AuthorizationService;
import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletBalanceRequestDTO;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletInsufficientBalanceException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import com.trackinvest.account.wallet.domain.exception.format.WalletAmountInvalidException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateWalletBalanceUseCaseTest {

    @Mock
    private WalletRepositoryPort walletRepository;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private UpdateWalletBalanceUseCase updateWalletBalanceUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID walletId = UUID.randomUUID();

    @Test
    void shouldDepositSuccessfully() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(50), true);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, userId, "wallet");
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = updateWalletBalanceUseCase.execute(userId, walletId, request);

        assertEquals(BigDecimal.valueOf(150), response.balance());
    }

    @Test
    void shouldWithdrawSuccessfully() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(30), false);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, userId, "wallet");
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = updateWalletBalanceUseCase.execute(userId, walletId, request);

        assertEquals(BigDecimal.valueOf(70), response.balance());
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(50), true);

        assertThrows(WalletNotFoundException.class, () ->
                updateWalletBalanceUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalanceForWithdrawal() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", user, BigDecimal.valueOf(10),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(50), false);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, userId, "wallet");

        assertThrows(WalletInsufficientBalanceException.class, () ->
                updateWalletBalanceUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        UUID otherUserId = UUID.randomUUID();
        UserDomain otherUser = UserDomain.create(otherUserId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", otherUser, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(50), true);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doThrow(new ResourceAccessDeniedException("wallet"))
                .when(authorizationService).verifyOwner(otherUserId, userId, "wallet");

        assertThrows(ResourceAccessDeniedException.class, () ->
                updateWalletBalanceUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(-5), true);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, userId, "wallet");

        assertThrows(WalletAmountInvalidException.class, () ->
                updateWalletBalanceUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }
}
