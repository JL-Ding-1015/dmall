package com.djl.dmall.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitDeadDelayConfig {

    @Bean
    public Exchange getOrderDelayExchange() {
        return new DirectExchange("user.order.delay.exchange", true, false);
    }

    @Bean
    public Queue getOrderDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "user.order.exchange");
        arguments.put("x-dead-letter-routing-key", "order");
        arguments.put("x-message-ttl", 20 * 1000);

        return new Queue("user.order.delay.queue", true, false, false, arguments);
    }

    @Bean
    public Binding getBinding1() {
        return new Binding("user.order.delay.queue", Binding.DestinationType.QUEUE,
                "user.order.delay.exchange", "order_delay", null);
    }

    //user.order.exchange
    @Bean
    public Exchange getOrderExchange() {
        return new DirectExchange("user.order.exchange", true, false);
    }

    @Bean
    public Queue getOrderQueue() {
        return new Queue("user.order.queue", true, false, false);
    }

    @Bean
    public Binding getBinding2() {
        return new Binding("user.order.queue", Binding.DestinationType.QUEUE,
                "user.order.exchange", "order", null);
    }
}
