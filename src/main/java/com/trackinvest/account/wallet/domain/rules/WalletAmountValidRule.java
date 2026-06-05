package com.trackinvest.account.wallet.domain.rules;

import com.trackinvest.account.wallet.domain.exception.format.WalletAmountInvalidException;
import java.math.BigDecimal;


public class WalletAmountValidRule {

    private WalletAmountValidRule() {}

    public static void validate(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletAmountInvalidException();
        }
    }
}
