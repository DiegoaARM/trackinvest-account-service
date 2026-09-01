package com.trackinvest.account.wallet.application.usecase;

import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.out.WalletRepositoryPort;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.models.valueobjects.CurrencyTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllWalletsUseCaseTest {

    @Mock
    private WalletRepositoryPort walletRepository;

    @InjectMocks
    private GetAllWalletsUseCase getAllWalletsUseCase;

    private final UUID userId = UUID.randomUUID();

    @Test
    void shouldReturnAllWalletsForUser() {
        WalletDomain wallet1 = WalletDomain.from(
                UUID.randomUUID(), "Wallet 1", BigDecimal.valueOf(100),
                CurrencyTypeEnum.USD, LocalDateTime.now(), LocalDateTime.now()
        );
        WalletDomain wallet2 = WalletDomain.from(
                UUID.randomUUID(), "Wallet 2", BigDecimal.valueOf(200),
                CurrencyTypeEnum.EUR, LocalDateTime.now(), LocalDateTime.now()
        );

        when(walletRepository.findByUserId(userId)).thenReturn(List.of(wallet1, wallet2));

        List<GetWalletResponseDTO> result = getAllWalletsUseCase.execute(userId);

        assertEquals(2, result.size());
        assertEquals("Wallet 1", result.get(0).name());
        assertEquals("Wallet 2", result.get(1).name());
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoWallets() {
        when(walletRepository.findByUserId(userId)).thenReturn(List.of());

        List<GetWalletResponseDTO> result = getAllWalletsUseCase.execute(userId);

        assertTrue(result.isEmpty());
    }
}
