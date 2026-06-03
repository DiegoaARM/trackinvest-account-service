package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.common.domain.exception.ResourceAccessDeniedException;
import com.trackinvest.account.common.domain.service.AuthorizationService;
import com.trackinvest.account.user.domain.models.UserDomain;
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

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private UpdateWalletUseCase updateWalletUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID walletId = UUID.randomUUID();

    @Test
    void shouldUpdateWalletNameSuccessfully() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("New Name");

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, userId, "wallet");
        when(walletRepository.existsByNameAndUserId(request.name(), userId)).thenReturn(false);
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = updateWalletUseCase.execute(userId, walletId, request);

        assertEquals("New Name", response.name());
        verify(walletRepository).save(wallet);
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("New Name");

        assertThrows(WalletNotFoundException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNewNameIsInvalid() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("AB");

        assertThrows(WalletNameInvalidException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("Existing Name");

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, userId, "wallet");
        when(walletRepository.existsByNameAndUserId(request.name(), userId)).thenReturn(true);

        assertThrows(WalletNameDuplicateException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldNotCheckDuplicateWhenNameIsUnchanged() {
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "Same Name", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("Same Name");

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, userId, "wallet");
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = updateWalletUseCase.execute(userId, walletId, request);

        assertEquals("Same Name", response.name());
        verify(walletRepository, never()).existsByNameAndUserId(any(), any());
        verify(walletRepository).save(wallet);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        UUID otherUserId = UUID.randomUUID();
        UserDomain otherUser = UserDomain.create(otherUserId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", otherUser, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO("New Name");

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doThrow(new ResourceAccessDeniedException("wallet"))
                .when(authorizationService).verifyOwner(otherUserId, userId, "wallet");

        assertThrows(ResourceAccessDeniedException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).save(any());
    }
}
