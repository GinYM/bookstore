package com.bookstore.config;

import org.apache.activemq.ActiveMQConnectionFactory;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {
    @Value("${spring.activemq.broker-url}")
    private String BROKER_URL;
    @Value("${spring.activemq.user}")
    String BROKER_USERNAME;
    @Value("${spring.activemq.password}")
    String BROKER_PASSWORD;

    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setUserName(BROKER_USERNAME);
        connectionFactory.setPassword(BROKER_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

//    @Bean
//    public MessageConverter jsonMessageConverter()
//    {
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("1-1");
        return factory;
    }
}
