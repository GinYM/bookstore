package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueProducer {
    @Value("${exchange.name}")
    private String exchange;

    @Value("${routingkey}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public QueueProducer(RabbitTemplate rabbitTemplate) {
        super();
        this.rabbitTemplate = rabbitTemplate;
    }
    public void produce(BookDTO bookDTO) {
        log.info("Storing notification...");
        //rabbitTemplate.setExchange(exchange);
        rabbitTemplate.convertAndSend(exchange, routingKey, bookDTO);
        log.info("Notification stored in queue successfully");
    }
}
