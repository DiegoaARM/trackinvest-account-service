package com.trackinvest.account.common.domain.service;

import com.trackinvest.account.common.domain.exception.UnauthorizedAccessException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public void verifyOwner(String resourceOwnerId, String contextUserId) {
        if (resourceOwnerId == null || !resourceOwnerId.equals(contextUserId)) {
            throw new UnauthorizedAccessException();
        }
    }
}
