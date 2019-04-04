package com.bookstore.service.impl;

import com.bookstore.Exception.BookException;
import com.bookstore.domain.*;
import com.bookstore.enums.ResultEnum;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;
import com.bookstore.service.RedisLock;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class orderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private RedisLock redisLock;

    private static final int TIMEOUT = 10*1000; // time out 10s

    @Override
    public Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress, Payment payment,
                             String shippingMethod, SysUser user) throws BookException {

        Order order = new Order();
        order.setOrderTotal(shoppingCart.getGrandTotal());
        order.setPayment(payment);
        order.setShippingAddress(shippingAddress);
        order.setShippingMethod(shippingMethod);
        order.setSysUser(user);
        order.setBillingAddress(billingAddress);

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        for(CartItem cartItem : cartItemList){
            Book book = cartItem.getBook();

            if(!redisLock.lock(book.getId().toString(), String.valueOf(TIMEOUT))){
                throw new BookException(ResultEnum.LOCK_ERROR);
            }

            cartItem.setOrder(order);
            book.setInStockNumber(book.getInStockNumber()-cartItem.getQty());

            redisLock.unlock(book.getId().toString(), String.valueOf(TIMEOUT));

        }

        order.setCartItemList(cartItemList);
        order.setOrderDate(Calendar.getInstance().getTime());
        shippingAddress.setOrder(order);
        billingAddress.setOrder(order);

        payment.setOrder(order);

        order = orderRepository.save(order);

        return order;
    }

    @Override
    public Order findOne(Long id) {
        return orderRepository.findById(id).get();
    }
}
