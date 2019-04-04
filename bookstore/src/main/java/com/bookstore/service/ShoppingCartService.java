package com.bookstore.service;

import com.bookstore.domain.ShoppingCart;
import com.bookstore.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface ShoppingCartService {
    ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);

    void clearShoppingCart(ShoppingCart shoppingCart);

}
