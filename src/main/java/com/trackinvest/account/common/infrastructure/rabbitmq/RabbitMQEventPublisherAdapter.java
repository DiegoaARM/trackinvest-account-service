package com.trackinvest.account.common.infrastructure.rabbitmq;

import com.trackinvest.account.common.application.ports.out.EventPublisherPort;
import com.trackinvest.account.common.domain.event.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventPublisherAdapter implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DOMAIN_EVENTS_EXCHANGE,
                event.getEventType(),
                event
        );
    }
}
