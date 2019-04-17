package com.bookstore.service.impl;

import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
@Slf4j
public class QueueProduceId {

    @Value("${queue.name.sendId}")
    private String queueNameId;

    @Value("${queue.name.sendId.all}")
    private String queueNameIdAll;

    @Value("${spring.activemq.broker-url}")
    private String sslUrl;

    @Value("${spring.activemq.user}")
    private String userName;

    @Value("${spring.activemq.password}")
    private String password;

//    @Value("${exchange.name.sendId}")
//    private String exchange;
//
//    @Value("${exchange.name.sendId.all}")
//    private String exchangeAll;
//
//    @Value("${routingkey.sendId}")
//    private String routingKey;
//
//    @Value("routingkey.sendId.all")
//    private String routingKeyAll;

    @Autowired
    JmsTemplate jmsTemplate;

//    private final RabbitTemplate rabbitTemplate;
//    @Autowired
//    public QueueProduceId(RabbitTemplate rabbitTemplate) {
//        super();
//        this.rabbitTemplate = rabbitTemplate;
//    }
    public void produce(String url) {
        log.info("Storing notification url {}...", url);

        /*
        ActiveMQSslConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;

        try{
            connectionFactory = new ActiveMQSslConnectionFactory(sslUrl);
            connectionFactory.setUserName(userName);
            connectionFactory.setPassword(password);
            connection = connectionFactory.createConnection();

            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(session.createQueue(queueNameId));
            TextMessage textMessage = session.createTextMessage(url);
            messageProducer.send(textMessage);

        }catch (Exception e){
            e.printStackTrace();
            log.info("error: {}", e.getMessage());
        }

        try {
            if (messageProducer != null) {
                messageProducer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Throwable ignore) {
        }
        */

        jmsTemplate.send(queueNameId, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(url);
            }
        });
        //rabbitTemplate.setExchange(exchange);
        //rabbitTemplate.convertAndSend(exchange, routingKey, url);
        log.info("Notification url {} stored in queue successfully", url);
    }

    public void produceAll(String url) {
        log.info("Storing all notification url {}...", url);

        /*
        ActiveMQSslConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;

        try{
            connectionFactory = new ActiveMQSslConnectionFactory(sslUrl);
            connectionFactory.setUserName(userName);
            connectionFactory.setPassword(password);
            connection = connectionFactory.createConnection();

            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(session.createQueue(queueNameIdAll));
            TextMessage textMessage = session.createTextMessage(url);
            messageProducer.send(textMessage);

        }catch (Exception e){
            e.printStackTrace();
            log.info("error: {}", e.getMessage());
        }

        try {
            if (messageProducer != null) {
                messageProducer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Throwable ignore) {
        }
        */


        jmsTemplate.send(queueNameIdAll, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(url);
            }
        });
        //rabbitTemplate.setExchange(exchange);
        //rabbitTemplate.convertAndSend(exchange, routingKey, url);
        //rabbitTemplate.convertAndSend(exchangeAll, routingKeyAll, url);
        log.info("Notification all url {} stored in queue successfully", url);
    }
}
