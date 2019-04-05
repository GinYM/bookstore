package com.bookstore.DTO;

import lombok.Data;

@Data
public class BookDTO {
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

}
