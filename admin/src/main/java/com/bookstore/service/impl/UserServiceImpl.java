package com.bookstore.service.impl;

import com.bookstore.domain.SysUser;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        return userRepository.findByUsername(username);
    }

    @Override
    public SysUser findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public SysUser createUser(SysUser sysUser, Set<UserRole> userRoles) throws Exception{
        SysUser localSysUser = userRepository.findByUsername(sysUser.getUsername());
        if(localSysUser !=null){
            LOG.info("sysUser {} already exists. Nothing will be done", sysUser.getUsername());
        }else{
            for(UserRole ur : userRoles){
                roleRepository.save(ur.getRole());
            }

            sysUser.getUserRoles().addAll(userRoles);
            localSysUser = userRepository.save(sysUser);
        }

        return localSysUser;
    }

    @Override
    public SysUser save(SysUser sysUser){
        return userRepository.save(sysUser);
    }
}
