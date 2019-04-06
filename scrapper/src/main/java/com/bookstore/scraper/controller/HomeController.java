package com.bookstore.scraper.controller;

import com.bookstore.scraper.DTO.BookDTO;
import com.bookstore.scraper.Service.BookScraper;
import com.bookstore.scraper.Service.impl.QueueProducer;
import com.netflix.discovery.converters.Auto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private BookScraper bookScraper;

    @Autowired
    private QueueProducer queueProducer;

    @RequestMapping("/")
    public String home(){
        return "scrapper";
    }

    @RequestMapping("/scrap/")
    public BookDTO scrapBook(@RequestParam("url") String url) throws Exception{
        BookDTO bookDTO = bookScraper.scrapOne(url);
        queueProducer.produce(bookDTO);
        return bookDTO;
    }
}
