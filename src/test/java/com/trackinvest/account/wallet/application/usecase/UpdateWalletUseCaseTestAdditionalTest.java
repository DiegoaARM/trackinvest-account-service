package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.common.domain.service.AuthorizationService;
import com.trackinvest.account.user.domain.models.UserDomain;
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

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private UpdateWalletUseCase updateWalletUseCase;

    @Test
    void shouldThrowWhenNameIsNull() {
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        UserDomain user = UserDomain.create(userId);
        WalletDomain wallet = WalletDomain.from(
                walletId, "Old Name", user, BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );

        UpdateWalletRequestDTO request = new UpdateWalletRequestDTO(null);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        doNothing().when(authorizationService).verifyOwner(userId, userId, "wallet");

        assertThrows(WalletNameInvalidException.class, () ->
                updateWalletUseCase.execute(userId, walletId, request));

        verify(walletRepository, never()).existsByNameAndUserId(any(), any());
        verify(walletRepository, never()).save(any());
    }
}
