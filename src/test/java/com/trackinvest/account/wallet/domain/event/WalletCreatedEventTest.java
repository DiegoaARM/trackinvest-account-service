package com.trackinvest.account.wallet.domain.event;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletCreatedEventTest {

    @Test
    void shouldCreateWalletCreatedEvent() {
        String walletId = "wallet-123";
        String userId = "user-123";
        String walletName = "My Wallet";
        String currency = "USD";
        BigDecimal balance = BigDecimal.valueOf(1000);

        WalletCreatedEvent event = new WalletCreatedEvent(walletId, userId, walletName, currency, balance);

        assertEquals(walletId, event.getAggregateId());
        assertEquals("wallet.created", event.getEventType());
        assertEquals(userId, event.getUserId());
        assertEquals(walletName, event.getWalletName());
        assertEquals(currency, event.getCurrency());
        assertEquals(balance, event.getBalance());
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredOn());
    }
}
