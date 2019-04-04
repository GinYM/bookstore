package com.bookstore.service.impl;

import com.bookstore.domain.*;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ModelService;
import com.bookstore.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private CartItemService cartItemService;

    @Override
    public void addCheckoutAttr(Model model, SysUser user,
                                ShippingAddress shippingAddress,
                                Payment payment,
                                BillingAddress billingAddress) {
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();

        if(userPaymentList.size() == 0){
            model.addAttribute("emptyPaymentList", true);
        }else{
            model.addAttribute("emptyPaymentList", false);
        }

        if(userShippingList.size() == 0){
            model.addAttribute("emptyShippingList", true);
        }else{
            model.addAttribute("emptyShippingList", false);
        }

        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", user.getShoppingCart());

        List<String> statelist = USConstants.listOfUSStatesCode;
        Collections.sort(statelist);
        model.addAttribute("statelist", statelist);


        model.addAttribute("userShippingList", userShippingList);
        model.addAttribute("userPaymentList", userPaymentList);
    }
}
