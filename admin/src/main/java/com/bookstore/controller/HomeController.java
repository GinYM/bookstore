package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(){
        return "redirect:/login";
    }

    @RequestMapping("/home")
    public String home(){
        return "redirect:/book/bookList";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

}
