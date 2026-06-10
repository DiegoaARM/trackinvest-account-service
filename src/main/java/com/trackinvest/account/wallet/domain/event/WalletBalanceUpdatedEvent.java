package com.trackinvest.account.wallet.domain.event;

import com.trackinvest.account.common.domain.event.BaseDomainEvent;
import java.math.BigDecimal;

public class WalletBalanceUpdatedEvent extends BaseDomainEvent {
    private final String userId;
    private final BigDecimal previousBalance;
    private final BigDecimal newBalance;
    private final String currency;

    public WalletBalanceUpdatedEvent(String walletId, String userId, BigDecimal previousBalance, BigDecimal newBalance, String currency) {
        super(walletId, "wallet.balance.updated");
        this.userId = userId;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
        this.currency = currency;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public String getCurrency() {
        return currency;
    }
}
