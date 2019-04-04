package com.bookstore.service.impl;

import com.bookstore.domain.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @CachePut(cacheNames = "bookCache", key="#book.id")
    public Book save(Book book) {
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
        bookRepository.deleteById(id);
    }
}
