package com.bookstore.service.impl;


import com.bookstore.DTO.BookDTO;
//import com.bookstore.domain.Book;
import com.bookstore.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Getter
public class QueueConsumer{

    private BookDTO bookDTO;
    private List<BookDTO> bookDTOList;
    @Autowired
    private BookService bookService;


    @JmsListener(destination = "${queue.name}")
    public void receiveMessage(final Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            //ObjectMessage objectMessage = (ObjectMessage) message;
            TextMessage textMessage = (TextMessage) message;
            //BookDTO bookDTO = (BookDTO)objectMessage.getObject();
            BookDTO bookDTO = objectMapper.readValue(textMessage.getText(), BookDTO.class);
            if(bookDTO!=null){
                log.info("receive book: {}", bookDTO);
                bookService.saveBookDTO(bookDTO);
                this.bookDTO = bookDTO;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

//        this.bookDTO = bookDTO;
//        //System.out.println("Received!");
//        log.info("receive book: {}", bookDTO);
//        bookService.saveBookDTO(bookDTO);
    }

    @JmsListener(destination = "${queue.name.all}")
    public void receiveMessageAll(final Message message) {
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            //ObjectMessage objectMessage = (ObjectMessage) message;
            TextMessage textMessage = (TextMessage) message;
            //List<BookDTO> bookDTOList = (List<BookDTO>)message.getBody(List.class);
            //ArrayList<BookDTO> bookDTOList = (ArrayList<BookDTO>)objectMessage.getObject();

            List<BookDTO> bookDTOList = objectMapper.readValue(textMessage.getText(),
                    new TypeReference<List<BookDTO>>(){});

            if(bookDTOList!=null){
                log.info("receive book number: {}", bookDTOList.size());
                this.bookDTOList = bookDTOList;
                for (BookDTO bookDTO : bookDTOList) {
                    bookService.saveBookDTO(bookDTO);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
