package com.bookstore.controller;

import com.bookstore.domain.*;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.*;
import com.bookstore.service.impl.UserSecurityService;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;
import com.bookstore.utility.USConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.UserDataHandler;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.*;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    private BCryptPasswordEncoder encoder = SecurityUtility.passwordEncoder();



    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/bookshelf")
    public String bookshelf(Model model){
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);
        return "bookshelf";
    }

    @RequestMapping("/bookDetail")
    public String bookDetail(
            @PathParam("id") Long id, Model model, Principal principal
            ){
        if(principal != null){
            String username = principal.getName();
            SysUser sysUser = userService.findByUsername(username);
            model.addAttribute("user", sysUser);
        }
        Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty", 1);
        return "bookDetail";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String newUserPost(HttpServletRequest request,
                              @ModelAttribute("email") String userEmail,
                              @ModelAttribute("username") String username,
                              @ModelAttribute("password") String password,
                              Model model) throws Exception{
        model.addAttribute("email", userEmail);
        model.addAttribute("username", username);

        if(userService.findByUsername(username) != null){
            model.addAttribute("usernameExists", true);
            return "register";
        }

        if(userService.findByEmail(userEmail) != null){
            model.addAttribute("emailExists", true);
            return "register";
        }
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setEmail(userEmail);

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        sysUser.setPassword(encryptedPassword);

        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(sysUser, role));
        userService.createUser(sysUser, userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(sysUser, token);
        String appUrl = "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();

        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(
                appUrl, request.getLocale(), token, sysUser, password);

        mailSender.send(email);
        model.addAttribute("emailSent", "true");

        return "register";
    }

    @RequestMapping("/register")
    public String newUser() {return "register";}

    @RequestMapping("/registerValid")
    public String registerHandler(Locale locale,
                           @RequestParam("token") String token, Model model) {
        PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

        if(passwordResetToken == null){
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/badRequest";
        }

        SysUser sysUser = passwordResetToken.getSysUser();
        String username = sysUser.getUsername();
        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", sysUser);

        return "profile/profileHome";
    }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String loginHandler(Model model){
//        model.addAttribute("classActiveLogin", true);
//        return "index";
//    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", true);
        return "login";
    }

    @RequestMapping(value = "/forget", method = RequestMethod.POST)
    public String forgetHandler(
            HttpServletRequest request,
            @ModelAttribute("email") String email,
            Model model
    ) {
        model.addAttribute("classActiveForgetPassword", true);
        SysUser sysUser = userService.findByEmail(email);

        if(sysUser == null){
            model.addAttribute("emailNotExist", true);
            return "forget";
        }

        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        sysUser.setPassword(encryptedPassword);

        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(sysUser, role));
        userService.save(sysUser);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(sysUser, token);
        String appUrl = "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();

        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(
                appUrl, request.getLocale(), token, sysUser, password);

        mailSender.send(newEmail);
        model.addAttribute("forgetPasswordEmailSent", "true");


        return "forget";
    }

    @RequestMapping("/forget")
    public String forget(
            Model model
    ) {
        return "forget";
    }

    @RequestMapping("profile")
    public String profile(
            Model model,
            Principal principal
    ) {
        //System.out.println("username: "+ username);
        String username = principal.getName();
        model.addAttribute("username", username);
        return "profile/profileHome";
    }

    @RequestMapping("profileEdit")
    public String profileEdit(
            Model model,
            Principal principal
    ) {
        String username = principal.getName();
        SysUser user = userService.findByUsername(username);
        String email = user.getEmail();
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        return "profile/profileEdit";
    }

    @RequestMapping(value = "profileEdit", method = RequestMethod.POST)
    public String profileEditHandler(
            Model model,
            Principal principal,
            @ModelAttribute("username") String username,
            @ModelAttribute("email") String email,
            @ModelAttribute("password") String password,
            @ModelAttribute("textNewPassword") String newPassword
    ) {
        String username0 = principal.getName();
        SysUser user = userService.findByUsername(username0);

        SysUser user1 = userService.findByUsername(username);

        //String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
//        if(!encoder.matches(user.getPassword(),encryptedPassword)){
        if(!encoder.matches(password,user.getPassword())){
            model.addAttribute("incorrectPassword", true);
//            System.out.println(user.getPassword());
//            System.out.println(encryptedPassword);
            return "profile/profileEdit";
        }

        if(username0.equals(username) == false && user1 != null){
            model.addAttribute("usernameExist", true);
            return "profile/profileEdit";
        }

        try{
            user1 = userService.findByEmail(email);
        }catch(Exception e){
            model.addAttribute("emailExist", true);
            return "profile/profileEdit";
        }

        if(email.equals(user.getEmail()) == false && user1!=null){
            model.addAttribute("emailExist", true);
            return "profile/profileEdit";
        }

        //System.out.println(newPassword);

        if(!StringUtils.isEmpty(newPassword)){
            //System.out.println(newPassword);
            String encryptedNewPassword = SecurityUtility.passwordEncoder().encode(newPassword);
            user.setPassword(encryptedNewPassword);
        }

        model.addAttribute("updateSuccess", true);

        user.setEmail(email);
        user.setUsername(username);
        userService.save(user);


        return "profile/profileEdit";
    }

    @RequestMapping("orderInfo")
    public String orderInfo(
            Model model,
            Principal principal
    ) {
        SysUser user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        model.addAttribute("orderList", user.getOrderList());

        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);

        List<String> statelist = USConstants.listOfUSStatesCode;
        Collections.sort(statelist);
        model.addAttribute("statelist", statelist);

        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveOrders", true);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);


        return "profile/orderInfo";
    }

    @RequestMapping("billingInfo")
    public String billingInfo(
            Model model,
            Principal principal
    ) {

        return "redirect:/listOfCreditCard";
    }

    @RequestMapping("shippingInfo")
    public String shippingInfo(
            Model model,
            Principal principal
    ) {
        String username = principal.getName();
        SysUser user = userService.findByUsername(username);

        model.addAttribute("sysUser", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);
        model.addAttribute("listOfCreditCard", true);
        model.addAttribute("listOfShippingAddresses", true);

        List<String> statelist = USConstants.listOfUSStatesCode;
        Collections.sort(statelist);
        model.addAttribute("statelist", statelist);
        model.addAttribute("classActiveEdit", true);

        return "profile/shippingInfo";
    }

    @RequestMapping("/listOfCreditCard")
    public String listOfCreditCard(Model model,Principal principal, HttpServletRequest request){

        SysUser user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        //model.addAttribute("orderList", );
        return "profile/billingInfo";
    }

    @RequestMapping("/listOfShippingAddresses")
    public String listOfShippingAddresses(Model model,Principal principal, HttpServletRequest request){
        SysUser user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        //model.addAttribute("orderList", );
        return "profile/shippingInfo";
    }



    @RequestMapping("/addNewCreditCard")
    public String addNewCreditCard(Model model, Principal principal){
        SysUser user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewCreditCard", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();

        model.addAttribute("userBilling", userBilling);
        model.addAttribute("userPayment", userPayment);

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        model.addAttribute("statelist", stateList);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        return "profile/billingInfo";

    }


    @RequestMapping("/addNewShippingAddress")
    public String addNewShippingAddress(Model model, Principal principal){
        SysUser user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);


        UserShipping userShipping = new UserShipping();

        model.addAttribute("userShipping", userShipping);

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        return "profile/shippingInfo";

    }

    @RequestMapping("/setDefaultPayment")
    public String setDefaultPayment(Model model, Principal principal){
        return "profile/billingInfo";
    }

    @RequestMapping(value = "/addNewCreditCard", method = RequestMethod.POST)
    public String addNewCreditCardHandler(
            Model model,
            Principal principal,
            @ModelAttribute("userPayment") UserPayment userPayment,
            @ModelAttribute("userBilling") UserBilling userBilling
    ){
        //userBilling.setId(userPayment.getId());
        //System.out.println(userPayment.getId()+" "+userBilling.getId());
        SysUser user = userService.findByUsername(principal.getName());
        userService.updateUserBilling(userBilling, userPayment, user);

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        List<String> statelist = USConstants.listOfUSStatesCode;
        Collections.sort(statelist);
        model.addAttribute("statelist", statelist);

        model.addAttribute("listOfCreditCard", true);
        model.addAttribute("addNewCreditCard", false);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        return "redirect:/listOfCreditCard";

    }

    @RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.POST)
    public String addNewShippingAddressHandler(
            Model model,
            Principal principal,
            @ModelAttribute("userShipping") UserShipping userShipping
    ){
        SysUser user = userService.findByUsername(principal.getName());
        userService.updateUserShipping(userShipping, user);

        model.addAttribute("user", user);
        model.addAttribute("userShippingList", user.getUserShippingList());


        model.addAttribute("listOfShippingAddresses", true);
        return "redirect:/listOfShippingAddresses";

    }

    @RequestMapping("/updateCreditCard")
    public String updateCreditCard(
            @ModelAttribute("id") Long creditCardId,
            Principal principal,
            Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getSysUser().getId()){
            return "badRequestPage";
        }else{
            model.addAttribute("user", user);
            UserBilling userBilling = userPayment.getUserBilling();
            model.addAttribute("userPayment", userPayment);
            model.addAttribute("userBilling", userBilling);
            List<String> statelist = USConstants.listOfUSStatesCode;
            Collections.sort(statelist);
            model.addAttribute("statelist", statelist);
            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());

            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("addNewCreditCard", true);

            return "profile/billingInfo";
        }
    }

    @RequestMapping("/removeCreditCard")
    public String removeCreditCard(
            @ModelAttribute("id") Long creditCardId,
            Principal principal,
            Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getSysUser().getId()){
            return "badRequestPage";
        }else{
            model.addAttribute("user", user);

            userPaymentService.removeById(creditCardId);



            List<String> statelist = USConstants.listOfUSStatesCode;
            Collections.sort(statelist);
            model.addAttribute("statelist", statelist);
            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());

            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("listOfCreditCard", true);

            return "redirect:/listOfCreditCard";
        }
    }

    @RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
    public String setDefaulrPaymentHandler(
            @ModelAttribute("defaultUserPaymentId") Long defaultPaymentId,
            Principal principal,
            Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());
        userService.setUserDefaultPayment(defaultPaymentId, user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        model.addAttribute("user", user);
        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("listOfCreditCard", true);

        return "redirect:/listOfCreditCard";
    }

    @RequestMapping("/updateUserShipping")
    public String updateShippingAddress(
            @ModelAttribute("id") Long shippingAddressId,
            Principal principal,
            Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());

        UserShipping userShipping = userShippingService.findById(shippingAddressId);

        if(user.getId() != userShipping.getSysUser().getId()){
            log.error("Error, user id doesn't match, userId:{}, providedId:{}", user.getId(), userShipping.getSysUser().getId());
            return "badRequestPage";
        }else{
            model.addAttribute("user", user);
            List<String> statelist = USConstants.listOfUSStatesCode;
            Collections.sort(statelist);
            model.addAttribute("userShipping", userShipping);
            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("stateList", statelist);

            return "profile/shippingInfo";
        }
    }

    @RequestMapping("/removeUserShipping")
    public String removeShippingAddress(
            @ModelAttribute("id") Long shippingId,
            Principal principal,
            Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(shippingId);

        if(user.getId() != userShipping.getSysUser().getId()){
            return "badRequestPage";
        }else{
            model.addAttribute("user", user);


            userShippingService.removeById(shippingId);



            List<String> statelist = USConstants.listOfUSStatesCode;
            Collections.sort(statelist);
            model.addAttribute("statelist", statelist);
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfShippingAddresses", true);

            return "redirect:/listOfShippingAddresses";
        }
    }

    @RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
    public String setDefaulrShippingHandler(
            @ModelAttribute("defaultShippingAddressId") Long defaultShippingId,
            Principal principal,
            Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());
        userService.setUserDefaultShipping(defaultShippingId, user);
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("user", user);
        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "redirect:/listOfShippingAddresses";
    }

    @RequestMapping("/orderDetail")
    public String orderDetail(
            @RequestParam("id") Long orderId,
            Principal principal, Model model
    ){
        SysUser user = userService.findByUsername(principal.getName());
        Order order = orderService.findOne(orderId);

        if(order.getSysUser().getId()!=user.getId()){
            return "badRequestPage";
        }else{
            List<CartItem> cartItemList = cartItemService.findByorder(order);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("user", user);
            model.addAttribute("order", order);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());

            model.addAttribute("orderList", user.getOrderList());

            UserShipping userShipping = new UserShipping();
            model.addAttribute("userShipping", userShipping);

            List<String> statelist = USConstants.listOfUSStatesCode;
            Collections.sort(statelist);
            model.addAttribute("statelist", statelist);

            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveOrders", true);
            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("displayOrderDetail", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfShippingAddresses", true);

            return "profile/orderInfo";

        }
    }


}
