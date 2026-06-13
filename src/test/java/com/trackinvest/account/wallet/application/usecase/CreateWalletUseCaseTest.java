package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.common.application.ports.out.EventPublisherPort;
import com.trackinvest.account.wallet.application.ports.in.dto.CreateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.exception.business.WalletMaxNumberException;
import com.trackinvest.account.wallet.domain.exception.business.WalletNameDuplicateException;
import com.trackinvest.account.wallet.domain.exception.format.WalletNameInvalidException;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.models.valueobjects.CurrencyTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateWalletUseCaseTest {

    @Mock
    private WalletRepositoryPort walletRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    @InjectMocks
    private CreateWalletUseCase createWalletUseCase;

    private final UUID userId = UUID.randomUUID();
    private final CreateWalletRequestDTO request = new CreateWalletRequestDTO("My Wallet", CurrencyTypeEnum.USD);

    @Test
    void shouldCreateWalletSuccessfully() {
        when(walletRepository.countByUserId(userId)).thenReturn(1L);
        when(walletRepository.existsByNameAndUserId(request.name(), userId)).thenReturn(false);
        when(walletRepository.save(any(WalletDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GetWalletResponseDTO response = createWalletUseCase.execute(userId, request);

        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.currency(), response.currency());

        ArgumentCaptor<WalletDomain> captor = ArgumentCaptor.forClass(WalletDomain.class);
        verify(walletRepository).save(captor.capture());
        WalletDomain saved = captor.getValue();
        assertEquals(request.name(), saved.getName());
        assertEquals(request.currency(), saved.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenNameIsInvalid() {
        CreateWalletRequestDTO invalidRequest = new CreateWalletRequestDTO("AB", CurrencyTypeEnum.USD);

        assertThrows(WalletNameInvalidException.class, () ->
                createWalletUseCase.execute(userId, invalidRequest));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenMaxWalletsReached() {
        when(walletRepository.countByUserId(userId)).thenReturn(10L);

        assertThrows(WalletMaxNumberException.class, () ->
                createWalletUseCase.execute(userId, request));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameDuplicated() {
        when(walletRepository.countByUserId(userId)).thenReturn(1L);
        when(walletRepository.existsByNameAndUserId(request.name(), userId)).thenReturn(true);

        assertThrows(WalletNameDuplicateException.class, () ->
                createWalletUseCase.execute(userId, request));

        verify(walletRepository, never()).save(any());
    }
}
