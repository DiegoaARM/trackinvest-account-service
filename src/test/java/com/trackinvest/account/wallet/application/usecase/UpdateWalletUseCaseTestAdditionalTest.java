package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
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
class UpdateWalletUseCaseTestAdditionalTest {

    @Mock
    private WalletRepositoryPort walletRepository;

    @InjectMocks
    private UpdateWalletUseCase updateWalletUseCase;

    @Test
    void shouldThrowWhenNameIsNull() {
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", null, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO(null);

        when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(WalletNameInvalidException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).existsByNameAndUserId(any(), any());
        verify(walletRepository, never()).save(any());
    }
}
