package com.trackinvest.account.wallet.domain.event;

import com.trackinvest.account.common.domain.event.BaseDomainEvent;
import java.math.BigDecimal;

public class WalletCreatedEvent extends BaseDomainEvent {
    private final String userId;
    private final String walletName;
    private final String currency;
    private final BigDecimal balance;

    public WalletCreatedEvent(String walletId, String userId, String walletName, String currency, BigDecimal balance) {
        super(walletId, "wallet.created");
        this.userId = userId;
        this.walletName = walletName;
        this.currency = currency;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public String getWalletName() {
        return walletName;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
