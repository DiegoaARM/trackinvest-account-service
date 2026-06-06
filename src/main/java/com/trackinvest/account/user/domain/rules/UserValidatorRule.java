package com.trackinvest.account.user.domain.rules;

import com.trackinvest.account.common.domain.exception.RequiredAttributeException;
import com.trackinvest.account.common.domain.rules.DomainRule;
import com.trackinvest.account.user.domain.models.UserDomain;

public class UserValidatorRule implements DomainRule<UserDomain> {

    @Override
    public UserDomain validate(UserDomain user) {
        if (user == null) {
            throw new RequiredAttributeException("user");
        }
        if (user.getId() == null) {
            throw new RequiredAttributeException("id");
        }
        if (user.getCognitoId() == null || user.getCognitoId().trim().isEmpty()) {
            throw new RequiredAttributeException("cognitoId");
        }
        if (user.getFullname() == null || user.getFullname().trim().isEmpty()) {
            throw new RequiredAttributeException("fullname");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RequiredAttributeException("email");
        }
        if (user.getCreatedAt() == null) {
            throw new RequiredAttributeException("createdAt");
        }
        if (user.getUpdatedAt() == null) {
            throw new RequiredAttributeException("updatedAt");
        }
        if (user.getWalletsList() == null) {
            throw new RequiredAttributeException("walletsList");
        }
        return user;
    }
}
