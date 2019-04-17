package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class QueueProducer {
//    @Value("${exchange.name}")
//    private String exchange;
//
//    @Value("${routingkey}")
//    private String routingKey;
//
//    @Value("${exchange.name.all}")
//    private String exchangeAll;
//
//    @Value("${routineKeyAll}")
//    private String routingKeyAll;

    @Value("${queue.name}")
    private String queueName;

    @Value("${queue.name.all}")
    private String queueNameAll;

    @Autowired
    private JmsTemplate jmsTemplate;

//    private final RabbitTemplate rabbitTemplate;
//    @Autowired
//    public QueueProducer(RabbitTemplate rabbitTemplate) {
//        super();
//        this.rabbitTemplate = rabbitTemplate;
//    }

    public void produce(BookDTO bookDTO) {
        log.info("Storing notification...");
        //rabbitTemplate.setExchange(exchange);
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            final String dtoStr = objectMapper.writeValueAsString(bookDTO);
            jmsTemplate.send(queueName, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(dtoStr);
                }
            });
        }catch(Exception e){
            log.error("[error] cannot transform object to json");
            e.printStackTrace();
            return;
        }

        //rabbitTemplate.convertAndSend(exchange, routingKey, bookDTO);
        log.info("Notification stored in queue successfully");
    }

    public void produceAll(List<BookDTO> bookDTOList) {
        log.info("Storing BookDTOList notification...");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<BookDTO> bookDTOArrayList = new ArrayList<>(bookDTOList);
        //String dtoStr="";
        try{
            final String dtoStr = objectMapper.writeValueAsString(bookDTOArrayList);
            jmsTemplate.send(queueNameAll, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(dtoStr);
                }
            });
        }catch(Exception e){
            log.error("[error] cannot transform object to json");
            e.printStackTrace();
            return;
        }


        //rabbitTemplate.setExchange(exchange);
        //rabbitTemplate.convertAndSend(exchangeAll, routingKeyAll, bookDTOList);
        log.info("Notification BookDTOList stored in queue successfully");
    }
}
