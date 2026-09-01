package com.trackinvest.account.common.domain.event;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseDomainEvent implements DomainEvent {

    public static final int SCHEMA_VERSION = 1;

    private final String eventId;
    private final String aggregateId;
    private final String eventType;
    private final Instant occurredOn;
    private final int version;
    private String correlationId;

    protected BaseDomainEvent(String aggregateId, String eventType) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.occurredOn = Instant.now();
        this.version = SCHEMA_VERSION;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }

    public int getVersion() {
        return version;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
