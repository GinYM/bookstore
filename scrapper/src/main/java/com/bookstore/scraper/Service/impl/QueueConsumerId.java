package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import com.bookstore.scraper.Service.BookScraper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        if(bookDTO != null){
            queueProducer.produce(bookDTO);
        }else{
            log.info("[error] bookDTO is empty");
        }

    }

    @RabbitListener(queues = "${queue.name.sendId.all}", containerFactory = "jsaFactory")
    public void receiveMessageAll(String url) {
        this.url = url;
        //System.out.println("Received!");
        // scrap book and send back
        log.info("[info] receive All url: {}", url);
        List<BookDTO> bookDTOList = bookScraper.scrapAll(url);
        if(bookDTOList != null && bookDTOList.size()>0){
            queueProducer.produceAll(bookDTOList);
        }else{
            log.info("[error] bookDTO is empty");
        }

    }
}
