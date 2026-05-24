package com.trackinvest.account.wallet.domain.rules;

import com.trackinvest.account.wallet.domain.exception.format.WalletAmountInvalidException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class WalletAmountValidRule {

    public static void validate(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletAmountInvalidException();
        }
    }
}
