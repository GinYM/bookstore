package com.bookstore.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BookToCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="cart_item_id")
    private CartItem cartItem;
}
