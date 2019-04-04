package com.bookstore.service.impl;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.ShippingAddress;
import com.bookstore.domain.UserBilling;
import com.bookstore.service.BillingAddressService;
import org.springframework.stereotype.Service;

@Service
public class BillingAddressServiceImpl implements BillingAddressService {
    @Override
    public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
        billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
        billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
        billingAddress.setBillingAddressName(userBilling.getUserBillingName());
        billingAddress.setBillingAddressState(userBilling.getUserBillingState());
        billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
        billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
        billingAddress.setBillingAddressZipcode(userBilling.getUserBillingZipcode());
        return billingAddress;
    }

    @Override
    public BillingAddress setByShippingAddress(ShippingAddress shippingAddress, BillingAddress billingAddress) {
        billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
        billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
        billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
        billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
        billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
        billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
        billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
        return billingAddress;
    }
}
