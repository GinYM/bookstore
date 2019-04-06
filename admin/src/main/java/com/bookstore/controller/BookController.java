package com.bookstore.controller;

import com.bookstore.DTO.BookDTO;
import com.bookstore.domain.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.impl.QueueConsumer;
import com.bookstore.service.impl.QueueProduceId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    private static final String urlPre = "http://scraper/scrap/?url=";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookService bookService;

    @Autowired
    private QueueConsumer queueConsumer;

    @Autowired
    private QueueProduceId queueProduceId;

    @RequestMapping(value = "/importFromUrl")
    public String importFromUrl(Model model){
        return "importFromUrl";
    }

    @PostMapping("/importFromUrl")
    public String importFromUrlHandler(
            @RequestParam("url") String url,
            Model model
    ){
        BookDTO bookDTO = queueConsumer.getBookDTO();
        model.addAttribute("url", url);
        if(queueConsumer.getBookDTO() == null || !bookDTO.getUrl().equals(url)){
            model.addAttribute("getSuccess", false);
            queueProduceId.produce(url);
            //restTemplate.getForObject(urlPre+url, BookDTO.class);
        }else{
            model.addAttribute("bookDTO", bookDTO);
            model.addAttribute("getSuccess", true);
        }

        return "importFromUrl";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "addBook";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBookPost(
            @ModelAttribute("book") Book book, HttpServletRequest request
    ) {
        // todo use more advanced method, such as aws to store the image
        bookService.save(book);
        MultipartFile bookImage = book.getBookImage();

        try {
            byte[] bytes = bookImage.getBytes();
            String name = book.getId() + ".png";
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/image/book/" + name)));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }

        return "redirect:bookList";
    }

    @RequestMapping("/bookList")
    public String bookList(Model model) {
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);
        return "bookList";
    }

    @RequestMapping("/bookInfo")
    public String bookInfo(@RequestParam("id") Long id, Model model) {
        Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        return "bookInfo";
    }

    @RequestMapping("/updateBook")
    public String updateBook(@RequestParam("id") Long id, Model model) {
        Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        return "updateBook";
    }

    @RequestMapping(value = "/updateBook", method = RequestMethod.POST)
    public String updateBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
        bookService.save(book);
        MultipartFile bookImage = book.getBookImage();
        if (!bookImage.isEmpty()) {
            try {
                byte[] bytes = bookImage.getBytes();
                String name = book.getId() + ".png";
                Path p = Paths.get("src/main/resources/static/image/book/" + name);
                if(Files.isRegularFile(p)){
                    Files.delete(p);
                }
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/image/book/" + name)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/book/bookInfo?id=" + book.getId();
    }

    @PostMapping("/remove")
    public String remove(
            @ModelAttribute("id") String id,
            Model model
    ){
        bookService.removeOne(Long.parseLong(id.substring(8)));
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);

        return "redirect:/book/bookList";
    }
}
