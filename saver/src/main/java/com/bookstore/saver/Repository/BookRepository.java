package com.bookstore.saver.Repository;

import com.bookstore.saver.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
