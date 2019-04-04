package com.bookstore.service;

import com.bookstore.domain.SysUser;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;

import java.util.Set;

public interface UserService {
    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final SysUser sysUser, final String token);

    SysUser findByUsername(String username);
    SysUser findByEmail(String email);

    SysUser createUser(SysUser sysUser, Set<UserRole> userRoles) throws Exception;

    SysUser createUserOrUpdate(SysUser sysUser, Set<UserRole> userRoles) throws Exception;

    SysUser save(SysUser sysUser);

    void updateUserBilling(UserBilling userBilling, UserPayment userPayment, SysUser user);
    void updateUserShipping(UserShipping userShipping, SysUser user);

    void setUserDefaultPayment(Long id, SysUser user);
    void setUserDefaultShipping(Long id, SysUser user);

}
