package com.trackinvest.account.user.domain.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserCreatedEventTest {

    @Test
    void shouldCreateUserCreatedEvent() {
        String userId = "user-123";
        String email = "test@example.com";
        String name = "Test User";

        UserCreatedEvent event = new UserCreatedEvent(userId, email, name);

        assertEquals(userId, event.getAggregateId());
        assertEquals("user.created", event.getEventType());
        assertEquals(email, event.getEmail());
        assertEquals(name, event.getName());
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredOn());
    }
}
