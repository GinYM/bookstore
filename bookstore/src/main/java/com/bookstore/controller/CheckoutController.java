package com.bookstore.controller;

import com.bookstore.Exception.BookException;
import com.bookstore.domain.*;
import com.bookstore.enums.ResultEnum;
import com.bookstore.service.*;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Controller
public class CheckoutController {

    private ShippingAddress shippingAddress = new ShippingAddress();
    private BillingAddress billingAddress = new BillingAddress();
    private Payment payment = new Payment();

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShippingAddressService shippingAddressService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillingAddressService billingAddressService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping("/checkout")
    public String checkout(
            @RequestParam("id") Long cartId,
            @RequestParam(value="missingRequiredField", required = false) boolean missingRequiredField,
            Model model, Principal principal
    ){
        SysUser user = userService.findByUsername(principal.getName());
        if(cartId != user.getShoppingCart().getId()){
            return "badRequestPage";
        }

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

        if(cartItemList.size()==0){
            model.addAttribute("emptyCart", true);
            return "forward:/shoppingCart/cart";
        }

        for(CartItem cartItem : cartItemList){
            if(cartItem.getBook().getInStockNumber()<cartItem.getQty()){
                model.addAttribute("notEnoughStock", true);
                return "forward:/shoppingCart/cart";
            }
        }

        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();
        model.addAttribute("userShippingList", userShippingList);
        model.addAttribute("userPaymentList", userPaymentList);

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

        ShoppingCart shoppingCart = user.getShoppingCart();
        for(UserShipping userShipping : userShippingList){
            if(userShipping.isUserShippingDefault()){
                shippingAddressService.setByUserShipping(userShipping, shippingAddress);
            }
        }

        for(UserPayment userPayment : userPaymentList){
            if(userPayment.isDefaultPayment()){
                paymentService.setByUserPayment(userPayment, payment);
                billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
            }
        }

        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", user.getShoppingCart());

        List<String> statelist = USConstants.listOfUSStatesCode;
        Collections.sort(statelist);
        model.addAttribute("statelist", statelist);

        model.addAttribute("classActiveShipping", true);

        if(missingRequiredField){
            model.addAttribute("missingRequiredField", true);
        }

        return "checkout";

    }

    @RequestMapping("/setShippingAddress")
    public String setShippingAddress(
            @RequestParam("userShippingId") Long userShippingId,
            Principal principal, Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(userShippingId);

        if(userShipping.getSysUser().getId() != user.getId()){
            return "badRequestPage";
        }else{
            shippingAddressService.setByUserShipping(userShipping, shippingAddress);

            modelService.addCheckoutAttr(model, user,shippingAddress,payment,billingAddress);

            model.addAttribute("classActiveShipping", true);

            return "checkout";

        }
    }

    @RequestMapping("/setPaymentMethod")
    public String setPaymentMethod(
            @RequestParam("userPaymentId") Long userPaymentId,
            Principal principal, Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(userPaymentId);
        UserBilling userBilling = userPayment.getUserBilling();

        if(userPayment.getSysUser().getId() != user.getId()){
            return "badRequestPage";
        }else{
            paymentService.setByUserPayment(userPayment, payment);
            billingAddressService.setByUserBilling(userBilling, billingAddress);

            modelService.addCheckoutAttr(model,user,shippingAddress,payment,billingAddress);

            model.addAttribute("classActivePayment", true);
            return "checkout";
        }
    }

    @PostMapping("/checkout")
    public String checkoutPost(
            @ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
            @ModelAttribute("billingAddress") BillingAddress billingAddress,
            @ModelAttribute("payment") Payment payment,
            @ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
            @ModelAttribute("shippingMethod") String shippingMethod,
            Principal principal, Model model
    ){
        ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();

        BigDecimal orderTotal = shoppingCart.getGrandTotal().multiply(new BigDecimal(1.06) );
        orderTotal.setScale(2, RoundingMode.HALF_UP);

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        model.addAttribute("cartItemList", cartItemList);
        if(billingSameAsShipping.equals("true")){
            billingAddressService.setByShippingAddress(shippingAddress,billingAddress);
        }

        if(shippingAddress.getShippingAddressStreet1().isEmpty() ||
                shippingAddress.getShippingAddressCity().isEmpty()||
                shippingAddress.getShippingAddressState().isEmpty()||
                shippingAddress.getShippingAddressName().isEmpty() ||
                shippingAddress.getShippingAddressZipcode().isEmpty()||
                payment.getCardNumber().isEmpty() ||
                payment.getCvc()==0||
                billingAddress.getBillingAddressStreet1().isEmpty() ||
                billingAddress.getBillingAddressCity().isEmpty()||
                billingAddress.getBillingAddressState().isEmpty()||
                billingAddress.getBillingAddressName().isEmpty() ||
                billingAddress.getBillingAddressZipcode().isEmpty()){
            return "redirect:/checkout?id="+shoppingCart.getId()+"&missingRequiredField=true";
        }

        SysUser user = userService.findByUsername(principal.getName());
        try{
            Order order = orderService.createOrder(shoppingCart,shippingAddress,billingAddress,payment,shippingMethod, user);
            mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
        }catch(BookException bookException){
            if(bookException.getCode().equals(ResultEnum.LOCK_ERROR.getCode())){
                model.addAttribute("orderFail", true);
                return "orderSubmittedPage";
            }
        }


        shoppingCartService.clearShoppingCart(shoppingCart);

        LocalDate today = LocalDate.now();
        LocalDate estimatedDeliveryDate;

        if(shippingMethod.equals("groundShipping")){
            estimatedDeliveryDate = today.plusDays(5);
        }else{
            estimatedDeliveryDate = today.plusDays(3);
        }

        model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);


        model.addAttribute("orderSuccess", true);

        model.addAttribute("orderTotal", orderTotal);

        return "orderSubmittedPage";

    }
}
