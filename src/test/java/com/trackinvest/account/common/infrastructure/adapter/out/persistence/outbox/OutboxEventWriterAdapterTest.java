package com.trackinvest.account.common.infrastructure.adapter.out.persistence.outbox;

import com.trackinvest.account.common.domain.event.BaseDomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboxEventWriterAdapterTest {

    @Mock
    private OutboxEventRepository outboxEventRepository;

    private OutboxEventWriterAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new OutboxEventWriterAdapter(outboxEventRepository);
    }

    @Test
    void shouldPersistEventToOutbox() throws Exception {
        BaseDomainEvent event = new TestEvent("wallet-123", "wallet.created");
        event.setCorrelationId("corr-789");

        when(outboxEventRepository.save(any(OutboxEventEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        adapter.publish(event);

        ArgumentCaptor<OutboxEventEntity> captor = ArgumentCaptor.forClass(OutboxEventEntity.class);
        verify(outboxEventRepository).save(captor.capture());

        OutboxEventEntity saved = captor.getValue();
        assertEquals(event.getEventId(), saved.getId().toString());
        assertEquals("wallet.created", saved.getEventType());
        assertEquals(TestEvent.class.getName(), saved.getTypeId());
        assertEquals("wallet-123", saved.getAggregateId());
        assertEquals("corr-789", saved.getCorrelationId());
        assertEquals(event.getOccurredOn(), saved.getOccurredOn());
        assertNotNull(saved.getCreatedAt());
        assertNull(saved.getPublishedAt());
        assertEquals(0, saved.getAttempts());

        assertTrue(saved.getPayload().contains("\"eventType\":\"wallet.created\""));
        assertTrue(saved.getPayload().contains("\"aggregateId\":\"wallet-123\""));
        assertTrue(saved.getPayload().contains("\"version\":1"));
    }

    @Test
    void shouldThrowWhenPayloadCannotBeSerialized() {
        BaseDomainEvent event = new UnserializableTestEvent("agg-1", "test.event");

        assertThrows(IllegalStateException.class, () -> adapter.publish(event));
    }

    private static class TestEvent extends BaseDomainEvent {
        public TestEvent(String aggregateId, String eventType) {
            super(aggregateId, eventType);
        }
    }

    private static class UnserializableTestEvent extends BaseDomainEvent {
        public UnserializableTestEvent(String aggregateId, String eventType) {
            super(aggregateId, eventType);
        }

        @Override
        public Instant getOccurredOn() {
            return null;
        }

        @Override
        public String getEventType() {
            throw new IllegalStateException("boom");
        }
    }
}
