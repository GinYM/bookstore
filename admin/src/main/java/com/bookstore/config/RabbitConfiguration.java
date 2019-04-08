package com.bookstore.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitConfiguration {
    @Value("${exchange.name}")
    private String fanoutExchange;
    @Value("${queue.name}")
    private String queueName;

    @Value("${exchange.name.all}")
    private String fanoutExchangeAll;
    @Value("${queue.name.all}")
    private String queueNameAll;

    @Bean
    Queue queueAll() {
        return new Queue(queueNameAll, true);
    }
    @Bean
    FanoutExchange exchangeAll() {
        return new FanoutExchange(fanoutExchangeAll);
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }
    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(fanoutExchange);
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }
    @Bean
    Binding binding1() {
        return BindingBuilder.bind(queueAll()).to(exchangeAll());
    }


    @Bean
    public MessageConverter jsonMessageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory,
                                                           SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
