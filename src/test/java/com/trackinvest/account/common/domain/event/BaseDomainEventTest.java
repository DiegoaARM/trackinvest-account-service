package com.trackinvest.account.common.domain.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class BaseDomainEventTest {

    @Test
    void shouldCreateConcreteEvent() {
        String aggregateId = "agg-123";
        String eventType = "test.event";

        TestEvent event = new TestEvent(aggregateId, eventType);

        assertNotNull(event.getEventId());
        assertEquals(aggregateId, event.getAggregateId());
        assertEquals(eventType, event.getEventType());
        assertNotNull(event.getOccurredOn());
        assertTrue(event.getOccurredOn().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    void shouldGenerateUniqueEventIds() {
        TestEvent event1 = new TestEvent("agg-1", "test.event");
        TestEvent event2 = new TestEvent("agg-2", "test.event");

        assertNotNull(event1.getEventId());
        assertNotNull(event2.getEventId());
        assertNotEquals(event1.getEventId(), event2.getEventId());
    }

    private static class TestEvent extends BaseDomainEvent {
        public TestEvent(String aggregateId, String eventType) {
            super(aggregateId, eventType);
        }
    }
}
