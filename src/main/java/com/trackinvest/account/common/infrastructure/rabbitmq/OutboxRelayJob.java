package com.trackinvest.account.common.infrastructure.rabbitmq;

import com.trackinvest.account.common.infrastructure.adapter.out.persistence.outbox.OutboxEventEntity;
import com.trackinvest.account.common.infrastructure.adapter.out.persistence.outbox.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class OutboxRelayJob {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelayJob.class);

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxRelayJob(OutboxEventRepository outboxEventRepository, RabbitTemplate rabbitTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelayString = "${outbox.relay.poll-interval-ms:2000}")
    @Transactional
    public void relayPendingEvents() {
        List<OutboxEventEntity> pendingEvents =
                outboxEventRepository.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc();

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.debug("Outbox relay picked up {} pending events", pendingEvents.size());

        for (OutboxEventEntity event : pendingEvents) {
            try {
                publishToRabbit(event);
                event.markPublished(Instant.now());
            } catch (Exception e) {
                event.recordFailure(e.getMessage());
                log.warn("Failed to publish outbox event [{}] attempt {}: {}",
                        event.getId(), event.getAttempts(), e.getMessage());
            }
        }
    }

    private void publishToRabbit(OutboxEventEntity event) {
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setMessageId(event.getId().toString());
        properties.setTimestamp(Date.from(event.getOccurredOn()));
        if (event.getCorrelationId() != null) {
            properties.setCorrelationId(event.getCorrelationId());
        }
        properties.setHeader("__TypeId__", event.getTypeId());

        Message message = new Message(event.getPayload().getBytes(StandardCharsets.UTF_8), properties);

        rabbitTemplate.send(RabbitMQConfig.DOMAIN_EVENTS_EXCHANGE, event.getEventType(), message);
    }
}
