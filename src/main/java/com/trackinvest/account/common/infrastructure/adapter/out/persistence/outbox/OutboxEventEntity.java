package com.trackinvest.account.common.infrastructure.adapter.out.persistence.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "outbox_events",
        indexes = @Index(name = "idx_outbox_pending", columnList = "publishedAt, createdAt")
)
public class OutboxEventEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 100)
    private String eventType;

    @Column(name = "type_id", nullable = false, length = 200)
    private String typeId;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @Column(nullable = false, columnDefinition = "text")
    private String payload;

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "occurred_on", nullable = false)
    private Instant occurredOn;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(nullable = false)
    private int attempts = 0;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    protected OutboxEventEntity() {
    }

    public static OutboxEventEntity from(UUID eventId, String eventType, String typeId,
                                         String aggregateId, String payload,
                                         String correlationId, Instant occurredOn) {
        OutboxEventEntity entity = new OutboxEventEntity();
        entity.id = eventId;
        entity.eventType = eventType;
        entity.typeId = typeId;
        entity.aggregateId = aggregateId;
        entity.payload = payload;
        entity.correlationId = correlationId;
        entity.occurredOn = occurredOn;
        entity.createdAt = Instant.now();
        return entity;
    }

    public void markPublished(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void recordFailure(String errorMessage) {
        this.attempts++;
        this.lastError = errorMessage;
    }

    public UUID getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getPayload() {
        return payload;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getLastError() {
        return lastError;
    }
}
