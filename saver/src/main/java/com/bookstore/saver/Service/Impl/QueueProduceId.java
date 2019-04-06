package com.bookstore.saver.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueProduceId {
    @Value("${exchange.name.sendId}")
    private String exchange;

    @Value("${routingkey.sendId}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public QueueProduceId(RabbitTemplate rabbitTemplate) {
        super();
        this.rabbitTemplate = rabbitTemplate;
    }
    public void produce(String url) {
        log.info("Storing notification url {}...", url);
        //rabbitTemplate.setExchange(exchange);
        //rabbitTemplate.convertAndSend(exchange, routingKey, url);
        rabbitTemplate.convertAndSend(exchange, routingKey, url);
        log.info("Notification url {} stored in queue successfully", url);
    }
}
