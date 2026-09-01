package com.trackinvest.account.common.domain.event;

import java.time.Instant;

public interface DomainEvent {
    String getEventId();
    String getAggregateId();
    String getEventType();
    Instant getOccurredOn();

    default String getCorrelationId() {
        return null;
    }
}
