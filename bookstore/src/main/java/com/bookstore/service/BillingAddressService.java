package com.bookstore.service;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.ShippingAddress;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;

public interface BillingAddressService {
    BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
    BillingAddress setByShippingAddress(ShippingAddress shippingAddress, BillingAddress billingAddress);
}
