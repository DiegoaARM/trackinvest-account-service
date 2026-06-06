package com.trackinvest.account.wallet.domain.rules;

import com.trackinvest.account.common.domain.exception.RequiredAttributeException;
import com.trackinvest.account.common.domain.rules.DomainRule;
import com.trackinvest.account.wallet.domain.models.WalletDomain;

public class WalletValidatorRule implements DomainRule<WalletDomain> {

    @Override
    public WalletDomain validate(WalletDomain wallet) {
        if (wallet == null) {
            throw new RequiredAttributeException("wallet");
        }
        if (wallet.getId() == null) {
            throw new RequiredAttributeException("id");
        }
        if (wallet.getName() == null || wallet.getName().trim().isEmpty()) {
            throw new RequiredAttributeException("name");
        }
        if (wallet.getUser() == null) {
            throw new RequiredAttributeException("user");
        }
        if (wallet.getBalance() == null) {
            throw new RequiredAttributeException("balance");
        }
        if (wallet.getCurrency() == null) {
            throw new RequiredAttributeException("currency");
        }
        if (wallet.getCreatedAt() == null) {
            throw new RequiredAttributeException("createdAt");
        }
        if (wallet.getUpdatedAt() == null) {
            throw new RequiredAttributeException("updatedAt");
        }
        return wallet;
    }
}
