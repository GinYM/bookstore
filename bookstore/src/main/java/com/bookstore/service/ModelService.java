package com.bookstore.service;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.Payment;
import com.bookstore.domain.ShippingAddress;
import com.bookstore.domain.SysUser;
import org.springframework.ui.Model;

public interface ModelService {
    void addCheckoutAttr(Model model, SysUser user,
                         ShippingAddress shippingAddress,
                         Payment payment,
                         BillingAddress billingAddress);
}
