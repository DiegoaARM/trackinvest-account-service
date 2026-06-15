package com.trackinvest.account.common.application.ports.out;

import com.trackinvest.account.common.domain.event.DomainEvent;

public interface EventPublisherPort {
    void publish(DomainEvent event);
}
