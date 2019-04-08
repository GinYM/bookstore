package com.bookstore.scraper.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookDTO implements Serializable {
    private long id = 123L;

    private String title;
    private String author;
    private String publisher;
    private String publicationDate;
    private String language;
    private String category;
    private int numberOfPages;
    private String format;
    private String isbn;
    private double shippingWeight;
    private double listPrice;
    private double ourPrice;
    private boolean active = true;
    private String description;
    private int inStockNumber;

    private String url;
    private String imgUrl;

    // for scrap all flag
    private String rawUrl;
    //private String imgAmzUrl;

}
