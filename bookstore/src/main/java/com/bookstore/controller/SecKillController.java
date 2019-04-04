package com.bookstore.controller;

import com.bookstore.domain.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private BookService bookService;

    private AtomicInteger sold = new AtomicInteger(0);

    @RequestMapping("/order")
    public String order(Model model){
        int current = sold.incrementAndGet();
        List<Book> bookList = bookService.findAll();
        Book book = bookList.get(0);
        model.addAttribute("total", "500");
        model.addAttribute("sold", current);
        return "seckillOrder";
    }

}
