package com.trackinvest.account.wallet.domain.rules;

import com.trackinvest.account.common.domain.exception.RequiredAttributeException;
import com.trackinvest.account.user.domain.models.UserDomain;
import com.trackinvest.account.wallet.domain.models.WalletDomain;
import com.trackinvest.account.wallet.domain.models.valueobjects.CurrencyTypeEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletValidatorRuleTest {

    private final WalletValidatorRule validator = new WalletValidatorRule();
    private final UUID id = UUID.randomUUID();
    private final UserDomain user = UserDomain.create(UUID.randomUUID());
    private final LocalDateTime now = LocalDateTime.now();
    private final WalletDomain validWallet = WalletDomain.from(id, "Test Wallet", user, BigDecimal.TEN, CurrencyTypeEnum.USD, now, now);

    @Test
    void shouldPassForValidWallet() {
        assertDoesNotThrow(() -> validator.validate(validWallet));
    }

    @Test
    void shouldThrowWhenWalletIsNull() {
        assertThrows(RequiredAttributeException.class, () -> validator.validate(null));
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        WalletDomain wallet = WalletDomain.from(null, "Test", user, BigDecimal.TEN, CurrencyTypeEnum.USD, now, now);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(wallet));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        WalletDomain wallet = WalletDomain.from(id, null, user, BigDecimal.TEN, CurrencyTypeEnum.USD, now, now);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(wallet));
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        WalletDomain wallet = WalletDomain.from(id, "   ", user, BigDecimal.TEN, CurrencyTypeEnum.USD, now, now);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(wallet));
    }

    @Test
    void shouldThrowWhenUserIsNull() {
        WalletDomain wallet = WalletDomain.from(id, "Test", null, BigDecimal.TEN, CurrencyTypeEnum.USD, now, now);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(wallet));
    }

    @Test
    void shouldThrowWhenBalanceIsNull() {
        WalletDomain wallet = WalletDomain.from(id, "Test", user, null, CurrencyTypeEnum.USD, now, now);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(wallet));
    }

    @Test
    void shouldThrowWhenCurrencyIsNull() {
        WalletDomain wallet = WalletDomain.from(id, "Test", user, BigDecimal.TEN, null, now, now);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(wallet));
    }

    @Test
    void shouldThrowWhenCreatedAtIsNull() {
        WalletDomain wallet = WalletDomain.from(id, "Test", user, BigDecimal.TEN, CurrencyTypeEnum.USD, null, now);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(wallet));
    }

    @Test
    void shouldThrowWhenUpdatedAtIsNull() {
        WalletDomain wallet = WalletDomain.from(id, "Test", user, BigDecimal.TEN, CurrencyTypeEnum.USD, now, null);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(wallet));
    }
}
