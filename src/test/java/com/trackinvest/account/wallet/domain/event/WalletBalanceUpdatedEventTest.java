package com.trackinvest.account.wallet.domain.event;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletBalanceUpdatedEventTest {

    @Test
    void shouldCreateWalletBalanceUpdatedEvent() {
        String walletId = "wallet-123";
        String userId = "user-123";
        BigDecimal previousBalance = BigDecimal.valueOf(500);
        BigDecimal newBalance = BigDecimal.valueOf(1000);
        String currency = "USD";

        WalletBalanceUpdatedEvent event = new WalletBalanceUpdatedEvent(walletId, userId, previousBalance, newBalance, currency);

        assertEquals(walletId, event.getAggregateId());
        assertEquals("wallet.balance.updated", event.getEventType());
        assertEquals(userId, event.getUserId());
        assertEquals(previousBalance, event.getPreviousBalance());
        assertEquals(newBalance, event.getNewBalance());
        assertEquals(currency, event.getCurrency());
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredOn());
    }
}
