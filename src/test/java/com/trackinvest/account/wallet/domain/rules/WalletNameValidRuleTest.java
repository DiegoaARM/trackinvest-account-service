package com.trackinvest.account.wallet.domain.rules;

import com.trackinvest.account.wallet.domain.exception.format.WalletNameInvalidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletNameValidRuleTest {

    @Test
    void shouldPassForValidName() {
        assertDoesNotThrow(() -> WalletNameValidRule.validate("My Wallet"));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(WalletNameInvalidException.class, () -> WalletNameValidRule.validate(null));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        assertThrows(WalletNameInvalidException.class, () -> WalletNameValidRule.validate(""));
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThrows(WalletNameInvalidException.class, () -> WalletNameValidRule.validate("   "));
    }

    @Test
    void shouldThrowWhenNameIsTooShort() {
        assertThrows(WalletNameInvalidException.class, () -> WalletNameValidRule.validate("AB"));
    }

    @Test
    void shouldThrowWhenNameIsTooLong() {
        String name = "A".repeat(26);
        assertThrows(WalletNameInvalidException.class, () -> WalletNameValidRule.validate(name));
    }
}
