package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
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
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage();
                objectMessage.setObject(bookDTO);
                return objectMessage;
            }
        });
        //rabbitTemplate.convertAndSend(exchange, routingKey, bookDTO);
        log.info("Notification stored in queue successfully");
    }

    public void produceAll(List<BookDTO> bookDTOList) {
        log.info("Storing BookDTOList notification...");
        ArrayList<BookDTO> bookDTOArrayList = new ArrayList<>(bookDTOList);
        jmsTemplate.send(queueNameAll, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage();
                objectMessage.setObject(bookDTOArrayList);
                return objectMessage;
            }
        });
        //rabbitTemplate.setExchange(exchange);
        //rabbitTemplate.convertAndSend(exchangeAll, routingKeyAll, bookDTOList);
        log.info("Notification BookDTOList stored in queue successfully");
    }
}
