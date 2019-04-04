package com.bookstore.scraper.Service;

import com.bookstore.scraper.DTO.BookDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookScraper {
    BookDTO scrapOne(String url);

    List<BookDTO> scrapList(List<String> urls);
}
