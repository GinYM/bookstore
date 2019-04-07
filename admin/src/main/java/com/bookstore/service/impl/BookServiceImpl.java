package com.bookstore.service.impl;

import com.bookstore.domain.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public Book save(Book book) {
        String name = endpointUrl+"/"+awsS3AudioBucket+"/"+book.getId().toString()+imgExt;
        if(book.getImgUrl() == null || !book.getImgUrl().equals(name)){
            book.setImgUrl(name);
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
}
