package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletNameDuplicateException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNotFoundException;
import com.trackinvest.account.wallet.domain.exception.format.WalletNameInvalidException;
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
class UpdateWalletUseCaseTest {

    @Mock
    private WalletRepositoryPort walletRepository;

    @InjectMocks
    private UpdateWalletUseCase updateWalletUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID walletId = UUID.randomUUID();

    @Test
    void shouldUpdateWalletNameSuccessfully() {
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", null, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("New Name");

        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.existsByNameAndUserId(request.name(), userId)).thenReturn(false);
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = updateWalletUseCase.execute(userId, walletId, request);

        assertEquals("New Name", response.name());
        verify(walletRepository).save(wallet);
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("New Name");

        assertThrows(WalletNotFoundException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNewNameIsInvalid() {
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", null, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("AB");

        assertThrows(WalletNameInvalidException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", null, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("Existing Name");

        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.existsByNameAndUserId(request.name(), userId)).thenReturn(true);

        assertThrows(WalletNameDuplicateException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldNotCheckDuplicateWhenNameIsUnchanged() {
        WalletDomain wallet = WalletDomain.from(
                walletId, "Same Name", null, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("Same Name");

        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = updateWalletUseCase.execute(userId, walletId, request);

        assertEquals("Same Name", response.name());
        verify(walletRepository, never()).existsByNameAndUserId(any(), any());
        verify(walletRepository).save(wallet);
    }

    @Test
    void shouldThrowExceptionWhenWalletDoesNotBelongToUser() {
        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(false);

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("New Name");

        assertThrows(WalletNotFoundException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }
}
