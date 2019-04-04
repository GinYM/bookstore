package com.bookstore.controller;

import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ResourceController {
    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/book/removeList", method = RequestMethod.POST)
    public String removeList(
            @RequestBody ArrayList<String> bookList, Model model
            ){
        for(String id : bookList){
            String bookId = id.substring(8);
            bookService.removeOne(Long.parseLong(bookId));
        }
        return "delete success";
    }

}
