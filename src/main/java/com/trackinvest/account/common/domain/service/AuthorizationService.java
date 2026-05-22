package com.trackinvest.account.common.domain.service;

import com.trackinvest.account.common.domain.exception.ResourceAccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService {

    public void verifyOwner(UUID resourceOwnerId, UUID contextUserId, String resourceName) {
        if (resourceOwnerId == null || !resourceOwnerId.equals(contextUserId)) {
            throw new ResourceAccessDeniedException(resourceName);
        }
    }
}
