package com.bookstore.saver.Service.Impl;

import com.bookstore.saver.DTO.BookDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class QueueConsumer {

    private BookDTO bookDTO;

    @RabbitListener(queues = "${queue.name}", containerFactory = "jsaFactory")
    public void receiveMessage(BookDTO bookDTO) {
        this.bookDTO = bookDTO;
        //System.out.println("Received!");
        log.info("receive book: {}", bookDTO);
    }
}
