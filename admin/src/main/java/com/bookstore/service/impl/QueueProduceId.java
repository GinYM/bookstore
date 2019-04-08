package com.bookstore.service.impl;

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

    @Value("${exchange.name.sendId.all}")
    private String exchangeAll;

    @Value("${routingkey.sendId}")
    private String routingKey;

    @Value("routingkey.sendId.all")
    private String routingKeyAll;

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

    public void produceAll(String url) {
        log.info("Storing all notification url {}...", url);
        //rabbitTemplate.setExchange(exchange);
        //rabbitTemplate.convertAndSend(exchange, routingKey, url);
        rabbitTemplate.convertAndSend(exchangeAll, routingKeyAll, url);
        log.info("Notification all url {} stored in queue successfully", url);
    }
}
