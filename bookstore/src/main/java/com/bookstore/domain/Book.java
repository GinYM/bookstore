package com.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Book implements Serializable {

    private static final long serialVersionUID = 123L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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

    @Column(columnDefinition = "text")
    private String description;
    private int inStockNumber;

    @Transient
    private MultipartFile bookImage;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookToCartItem> bookToCartItemList;


}
