package com.bookstore.service;

import com.bookstore.DTO.BookDTO;
import com.bookstore.domain.Book;

import java.util.List;

public interface BookService {
    Book save(Book book, boolean checkImgUrl);

    Book saveBookDTO(BookDTO bookDTO);

    List<Book> findAll();

    Book findOne(Long id);

    void removeOne(Long id);
    Book findByTitle(String title);
}
