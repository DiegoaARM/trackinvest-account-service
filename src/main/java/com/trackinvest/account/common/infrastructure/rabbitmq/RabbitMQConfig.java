package com.trackinvest.account.common.infrastructure.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String DOMAIN_EVENTS_EXCHANGE = "trackinvest.domain.events";

    @Bean
    public TopicExchange domainEventsExchange() {
        return new TopicExchange(DOMAIN_EVENTS_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
