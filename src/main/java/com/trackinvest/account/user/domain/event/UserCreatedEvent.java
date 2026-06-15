package com.trackinvest.account.user.domain.event;

import com.trackinvest.account.common.domain.event.BaseDomainEvent;

public class UserCreatedEvent extends BaseDomainEvent {
    private final String email;
    private final String name;

    public UserCreatedEvent(String userId, String email, String name) {
        super(userId, "user.created");
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
