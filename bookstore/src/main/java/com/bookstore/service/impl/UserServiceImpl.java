package com.bookstore.service.impl;

import com.bookstore.domain.*;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.*;
import com.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private UserBillingRepository userBillingRepository;

    @Autowired
    private UserShippingRepository userShippingRepository;

    @Override
    public PasswordResetToken getPasswordResetToken(final String token){
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void createPasswordResetTokenForUser(final SysUser sysUser, final String token){
        final PasswordResetToken myToken = new PasswordResetToken(token, sysUser);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public SysUser findByUsername(String username){
        return sysUserRepository.findByUsername(username);
    }

    @Override
    public SysUser findByEmail(String email){
        return sysUserRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public SysUser createUser(SysUser sysUser, Set<UserRole> userRoles) throws Exception{
        SysUser localSysUser = sysUserRepository.findByUsername(sysUser.getUsername());
        if(localSysUser !=null){
            LOG.info("sysUser {} already exists. Nothing will be done", sysUser.getUsername());
        }else{
            for(UserRole ur : userRoles){
                roleRepository.save(ur.getRole());
            }

            sysUser.getUserRoles().addAll(userRoles);

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setSysUser(sysUser);
            sysUser.setShoppingCart(shoppingCart);

            sysUser.setUserShippingList(new ArrayList<>());
            sysUser.setUserPaymentList(new ArrayList<>());


            localSysUser = sysUserRepository.save(sysUser);
        }

        return localSysUser;
    }

    @Override
    public SysUser createUserOrUpdate(SysUser sysUser, Set<UserRole> userRoles) throws Exception {
        SysUser localSysUser = sysUserRepository.findByUsername(sysUser.getUsername());
        if(localSysUser !=null){
            LOG.info("sysUser {} already exists. Only update", sysUser.getUsername());
            sysUser.setId(localSysUser.getId());
            localSysUser = sysUserRepository.save(sysUser);
        }else{
            for(UserRole ur : userRoles){
                roleRepository.save(ur.getRole());
            }

            sysUser.getUserRoles().addAll(userRoles);

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setSysUser(sysUser);
            sysUser.setShoppingCart(shoppingCart);

            sysUser.setUserShippingList(new ArrayList<>());
            sysUser.setUserPaymentList(new ArrayList<>());


            localSysUser = sysUserRepository.save(sysUser);
        }

        return localSysUser;
    }

    @Override
    public SysUser save(SysUser sysUser){
        return sysUserRepository.save(sysUser);
    }

    @Override
    public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, SysUser user){
        userPayment.setSysUser(user);
        userPayment.setUserBilling(userBilling);
        userPayment.setDefaultPayment(true);
        userBilling.setUserPayment(userPayment);
        user.getUserPaymentList().add(userPayment);
        save(user);
    }

    @Override
    public void updateUserShipping(UserShipping userShipping, SysUser user){
        userShipping.setSysUser(user);
        userShipping.setUserShippingDefault(true);
        user.getUserShippingList().add(userShipping);
        save(user);
    }

    @Override
    public void setUserDefaultPayment(Long id, SysUser user){
        List<UserPayment> userPaymentList = user.getUserPaymentList();

        for(UserPayment userPayment : userPaymentList){
            if(userPayment.getId() == id){
                userPayment.setDefaultPayment(true);

            }else{
                userPayment.setDefaultPayment(false);

            }
            userPaymentRepository.save(userPayment);
        }
    }

    @Override
    public void setUserDefaultShipping(Long id, SysUser user) {
        List<UserShipping> userShippingList = user.getUserShippingList();
        for(UserShipping userShipping : userShippingList){
            if(userShipping.getId().equals(id)){
                userShipping.setUserShippingDefault(true);
            }else{
                userShipping.setUserShippingDefault(false);
            }
            userShippingRepository.save(userShipping);
        }
    }
}
