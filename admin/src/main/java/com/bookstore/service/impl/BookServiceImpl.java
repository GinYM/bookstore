package com.bookstore.service.impl;

import com.bookstore.DTO.BookDTO;
import com.bookstore.domain.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Value("${aws.s3.audio.bucket}")
    private String awsS3AudioBucket;

    @Value("${aws.endpointUrl}")
    private String endpointUrl;

    @Value("${aws.imgExt}")
    private String imgExt;

    @Autowired
    private BookRepository bookRepository;

    private AmazonS3ClientServiceImpl amazonClient;

    @CachePut(cacheNames = "bookCache", key="#book.id")
    public Book save(Book book, boolean checkImgUrl) {
        if(checkImgUrl){
            String name = endpointUrl+"/"+awsS3AudioBucket+"/"+book.getId().toString()+imgExt;
            if(book.getImgUrl() == null || !book.getImgUrl().equals(name)){
                book.setImgUrl(name);
            }
        }

        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return (List<Book>) bookRepository.findAll();
    }

    @Cacheable(cacheNames = "bookCache", key= "#id")
    public Book findOne(Long id) {
        return bookRepository.findById(id).get();
    }

    @Override
    public void removeOne(Long id) {
        Book book = bookRepository.findById(id).get();
        amazonClient.deleteFileFromS3Bucket(book.getId().toString()+imgExt);
        bookRepository.deleteById(id);
    }

    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public Book saveBookDTO(BookDTO bookDTO) {
        Book book = null;
        if(findByTitle(bookDTO.getTitle()) == null){
            book = new Book();
            BeanUtils.copyProperties(bookDTO, book);
            save(book, false);
            log.info("new book, add to database");
        }else{
            log.info("the book {} already exists", bookDTO.getTitle());
        }

        return book;
    }
}
