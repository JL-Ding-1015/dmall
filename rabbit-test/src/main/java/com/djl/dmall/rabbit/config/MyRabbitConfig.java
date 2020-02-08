package com.djl.dmall.rabbit.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitConfig {

    @Bean
    public Queue helloQueue(){
        return new Queue("order-queue", true, false, false);
    }

    @Bean
    public Exchange getExchange(){
        return new DirectExchange("order-exchange", true, false);
    }

    @Bean
    public Binding getBinding(){
        return new Binding("order-queue", Binding.DestinationType.QUEUE,"order-exchange",
                "createOrder",null);
    }


}
