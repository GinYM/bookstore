package com.bookstore.saver.controller;

import com.bookstore.saver.DTO.BookDTO;
import com.bookstore.saver.Repository.BookRepository;
import com.bookstore.saver.Service.Impl.QueueConsumer;
import com.bookstore.saver.Service.Impl.QueueProduceId;
import com.bookstore.saver.domain.Book;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class HomeController {
    private static final String urlPre = "http://scraper/scrap/?url=";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QueueConsumer queueConsumer;

    @Autowired
    private QueueProduceId queueProduceId;


    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/")
    public String saveService(
            @RequestParam("url") String url,
            Model model
    ){

        model.addAttribute("url", url);


        BookDTO bookDTO = queueConsumer.getBookDTO();
        if(queueConsumer.getBookDTO() == null || !bookDTO.getUrl().equals(url)){
            model.addAttribute("getSuccess", false);
            queueProduceId.produce(url);
            //restTemplate.getForObject(urlPre+url, BookDTO.class);
        }else{
            model.addAttribute("bookDTO", bookDTO);
            model.addAttribute("getSuccess", true);
        }





//        Book book = new Book();
//        BeanUtils.copyProperties(bookDTO, book);
//        bookRepository.save(book);

        return "index";
    }


}
