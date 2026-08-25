package com.trackinvest.account.common.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trackinvest.account.common.domain.event.BaseDomainEvent;
import com.trackinvest.account.common.infrastructure.adapter.out.persistence.outbox.OutboxEventEntity;
import com.trackinvest.account.common.infrastructure.adapter.out.persistence.outbox.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxRelayJobTest {

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper;

    private OutboxRelayJob relayJob;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        relayJob = new OutboxRelayJob(outboxEventRepository, rabbitTemplate);
    }

    @Test
    void shouldPublishPendingEventsAndMarkThemAsPublished() {
        OutboxEventEntity event = buildPendingEvent("wallet.balance.updated", "corr-123");
        when(outboxEventRepository.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc())
                .thenReturn(List.of(event));

        relayJob.relayPendingEvents();

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).send(
                eq(RabbitMQConfig.DOMAIN_EVENTS_EXCHANGE),
                eq("wallet.balance.updated"),
                messageCaptor.capture()
        );

        Message sent = messageCaptor.getValue();
        assertEquals(event.getPayload(), new String(sent.getBody(), StandardCharsets.UTF_8));
        assertEquals("application/json", sent.getMessageProperties().getContentType());
        assertEquals(event.getId().toString(), sent.getMessageProperties().getMessageId());
        assertEquals("corr-123", sent.getMessageProperties().getCorrelationId());
        assertEquals(TestEvent.class.getName(), sent.getMessageProperties().getHeaders().get("__TypeId__"));

        assertNotNull(event.getPublishedAt());
        assertEquals(0, event.getAttempts());
    }

    @Test
    void shouldRecordFailureWhenRabbitPublishFails() {
        OutboxEventEntity event = buildPendingEvent("wallet.created", null);
        when(outboxEventRepository.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc())
                .thenReturn(List.of(event));
        doThrow(new RuntimeException("connection refused"))
                .when(rabbitTemplate).send(anyString(), anyString(), any(Message.class));

        relayJob.relayPendingEvents();

        assertNull(event.getPublishedAt());
        assertEquals(1, event.getAttempts());
        assertEquals("connection refused", event.getLastError());

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).send(
                eq(RabbitMQConfig.DOMAIN_EVENTS_EXCHANGE),
                eq("wallet.created"),
                messageCaptor.capture()
        );
        Message sent = messageCaptor.getValue();
        assertNull(sent.getMessageProperties().getCorrelationId());
    }

    @Test
    void shouldContinueProcessingRemainingEventsWhenOneFails() {
        OutboxEventEntity failingEvent = buildPendingEvent("first.event", null);
        OutboxEventEntity succeedingEvent = buildPendingEvent("second.event", null);
        when(outboxEventRepository.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc())
                .thenReturn(List.of(failingEvent, succeedingEvent));
        doThrow(new RuntimeException("boom"))
                .when(rabbitTemplate).send(anyString(), eq("first.event"), any(Message.class));

        relayJob.relayPendingEvents();

        assertNull(failingEvent.getPublishedAt());
        assertNotNull(succeedingEvent.getPublishedAt());
    }

    @Test
    void shouldDoNothingWhenThereAreNoPendingEvents() {
        when(outboxEventRepository.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc())
                .thenReturn(List.of());

        relayJob.relayPendingEvents();

        verify(rabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));
    }

    private OutboxEventEntity buildPendingEvent(String eventType, String correlationId) {
        BaseDomainEvent domainEvent = new TestEvent("agg-1", eventType);
        domainEvent.setCorrelationId(correlationId);
        try {
            String payload = objectMapper.writeValueAsString(domainEvent);
            return OutboxEventEntity.from(
                    java.util.UUID.fromString(domainEvent.getEventId()),
                    eventType,
                    TestEvent.class.getName(),
                    "agg-1",
                    payload,
                    correlationId,
                    Instant.now()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class TestEvent extends BaseDomainEvent {
        public TestEvent(String aggregateId, String eventType) {
            super(aggregateId, eventType);
        }
    }
}
