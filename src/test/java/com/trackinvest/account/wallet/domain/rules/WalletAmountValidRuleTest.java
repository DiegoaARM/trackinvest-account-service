package com.trackinvest.account.wallet.domain.rules;

import com.trackinvest.account.wallet.domain.exception.format.WalletAmountInvalidException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletAmountValidRuleTest {

    @Test
    void shouldPassForValidAmount() {
        assertDoesNotThrow(() -> WalletAmountValidRule.validate(BigDecimal.valueOf(100)));
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        assertThrows(WalletAmountInvalidException.class, () -> WalletAmountValidRule.validate(null));
    }

    @Test
    void shouldThrowWhenAmountIsZero() {
        assertThrows(WalletAmountInvalidException.class, () -> WalletAmountValidRule.validate(BigDecimal.ZERO));
    }

    @Test
    void shouldThrowWhenAmountIsNegative() {
        assertThrows(WalletAmountInvalidException.class, () -> WalletAmountValidRule.validate(BigDecimal.valueOf(-1)));
    }
}
