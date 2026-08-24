package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.common.application.ports.out.EventPublisherPort;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletBalanceRequestDTO;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletInsufficientBalanceException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import com.trackinvest.account.wallet.domain.exception.format.WalletAmountInvalidException;
import com.trackinvest.account.wallet.domain.event.WalletBalanceUpdatedEvent;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.models.valueobjects.CurrencyTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private EventPublisherPort eventPublisher;

    @InjectMocks
    private UpdateWalletBalanceUseCase updateWalletBalanceUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID walletId = UUID.randomUUID();

    @Test
    void shouldDepositSuccessfully() {
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", null, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(50), true);

        when(walletRepository.findByIdAndUserId(walletId, userId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = updateWalletBalanceUseCase.execute(userId, walletId, request);

        assertEquals(BigDecimal.valueOf(150), response.balance());
        assertPublishedEvent(BigDecimal.valueOf(100), BigDecimal.valueOf(150));
    }

    @Test
    void shouldWithdrawSuccessfully() {
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", null, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(30), false);

        when(walletRepository.findByIdAndUserId(walletId, userId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = updateWalletBalanceUseCase.execute(userId, walletId, request);

        assertEquals(BigDecimal.valueOf(70), response.balance());
        assertPublishedEvent(BigDecimal.valueOf(100), BigDecimal.valueOf(70));
    }

    private void assertPublishedEvent(BigDecimal expectedPreviousBalance, BigDecimal expectedNewBalance) {
        ArgumentCaptor<WalletBalanceUpdatedEvent> captor = ArgumentCaptor.forClass(WalletBalanceUpdatedEvent.class);
        verify(eventPublisher).publish(captor.capture());

        WalletBalanceUpdatedEvent event = captor.getValue();
        assertEquals(userId.toString(), event.getUserId());
        assertEquals(expectedPreviousBalance, event.getPreviousBalance());
        assertEquals(expectedNewBalance, event.getNewBalance());
        assertEquals(CurrencyTypeEnum.USD.name(), event.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenWalletIsNotFoundOrNotOwnedByUser() {
        when(walletRepository.findByIdAndUserId(walletId, userId)).thenReturn(Optional.empty());

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(50), true);

        assertThrows(WalletNotFoundException.class, () ->
                updateWalletBalanceUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalanceForWithdrawal() {
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", null, BigDecimal.valueOf(10),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(50), false);

        when(walletRepository.findByIdAndUserId(walletId, userId)).thenReturn(Optional.of(wallet));

        assertThrows(WalletInsufficientBalanceException.class, () ->
                updateWalletBalanceUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {
        WalletDomain wallet = WalletDomain.from(
                walletId, "My Wallet", null, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletBalanceRequestDTO request = new UpdateWalletBalanceRequestDTO(BigDecimal.valueOf(-5), true);

        when(walletRepository.findByIdAndUserId(walletId, userId)).thenReturn(Optional.of(wallet));

        assertThrows(WalletAmountInvalidException.class, () ->
                updateWalletBalanceUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}
