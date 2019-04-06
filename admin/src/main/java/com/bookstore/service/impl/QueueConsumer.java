package com.bookstore.service.impl;


import com.bookstore.DTO.BookDTO;
import com.bookstore.domain.Book;
import com.bookstore.service.BookService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class QueueConsumer {

    private BookDTO bookDTO;
    @Autowired
    private BookService bookService;

    @RabbitListener(queues = "${queue.name}", containerFactory = "jsaFactory")
    public void receiveMessage(BookDTO bookDTO) {
        this.bookDTO = bookDTO;
        //System.out.println("Received!");
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        bookService.save(book);
        log.info("receive book: {}", bookDTO);
    }
}
