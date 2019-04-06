package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import com.bookstore.scraper.Service.BookScraper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class QueueConsumerId {

    @Autowired
    private BookScraper bookScraper;
    @Autowired
    private QueueProducer queueProducer;

    private String url;
    @RabbitListener(queues = "${queue.name.sendId}", containerFactory = "jsaFactory")
    public void receiveMessage(String url) {
        this.url = url;
        //System.out.println("Received!");
        // scrap book and send back
        log.info("receive url: {}", url);
        BookDTO bookDTO = bookScraper.scrapOne(url);
        queueProducer.produce(bookDTO);
    }
}
