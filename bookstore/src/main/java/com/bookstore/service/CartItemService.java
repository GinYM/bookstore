package com.bookstore.service;

import com.bookstore.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartItemService {

    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
    List<CartItem> findByorder(Order order);
    CartItem updateCartItem(CartItem cartItem);
    CartItem addBookToCartItem(Book book, SysUser user, Integer qty);
    CartItem findById(Long id);
    void removeCartItem(CartItem cartItem);
}
