package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import com.bookstore.scraper.Service.BookScraper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;
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
    @JmsListener(destination = "${queue.name.sendId}")
    public void receiveMessage(Message url) {
        log.info("receive url: {}", url);
        try{
            this.url = ((TextMessage) url).getText();
            //System.out.println("Received!");
            // scrap book and send back
            BookDTO bookDTO = bookScraper.scrapOne(this.url);
            if(bookDTO != null){
                queueProducer.produce(bookDTO);
            }else{
                log.info("[error] bookDTO is empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @JmsListener(destination = "${queue.name.sendId.all}")
    public void receiveMessageAll(Message url) {
        log.info("[info] receive All url: {}", url);
        try{
            this.url =  ((TextMessage) url).getText();
            //System.out.println("Received!");
            // scrap book and send back
            List<BookDTO> bookDTOList = bookScraper.scrapAll(this.url);
            if(bookDTOList != null && bookDTOList.size()>0){
                queueProducer.produceAll(bookDTOList);
            }else{
                log.info("[error] bookDTO is empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
