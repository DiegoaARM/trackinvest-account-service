package com.trackinvest.account.common.infrastructure.adapter.out.persistence.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trackinvest.account.common.application.ports.out.EventPublisherPort;
import com.trackinvest.account.common.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OutboxEventWriterAdapter implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventWriterAdapter.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final OutboxEventRepository outboxEventRepository;

    public OutboxEventWriterAdapter(OutboxEventRepository outboxEventRepository) {
        this.outboxEventRepository = outboxEventRepository;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String payload = OBJECT_MAPPER.writeValueAsString(event);

            outboxEventRepository.save(OutboxEventEntity.from(
                    UUID.fromString(event.getEventId()),
                    event.getEventType(),
                    event.getClass().getName(),
                    event.getAggregateId(),
                    payload,
                    event.getCorrelationId(),
                    event.getOccurredOn()
            ));
        } catch (Exception e) {
            log.error("Failed to persist domain event [{}] to the outbox: {}", event.getEventType(), e.getMessage(), e);
            throw new IllegalStateException("Failed to persist domain event to the outbox", e);
        }
    }
}
