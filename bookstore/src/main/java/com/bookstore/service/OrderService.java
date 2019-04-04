package com.bookstore.service;

import com.bookstore.Exception.BookException;
import com.bookstore.domain.*;

public interface OrderService {
    Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress,
                      BillingAddress billingAddress, Payment payment,
                      String shippingMethod, SysUser user) throws BookException;

    Order findOne(Long id);
}
