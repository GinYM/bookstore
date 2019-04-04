package com.bookstore;

import com.bookstore.domain.SysUser;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.RoleRepository;
import com.bookstore.service.UserService;
import com.bookstore.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableCaching
public class BookstoreApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Override
    public void run(String ... args) throws Exception {

        SysUser sysUser1 = new SysUser();
        sysUser1.setFirstName("Eamon");
        sysUser1.setLastName("Albert");
        sysUser1.setUsername("YM");
        sysUser1.setPassword(SecurityUtility.passwordEncoder().encode("demo"));
        sysUser1.setEmail("EAlbert@gmail.com");
        Set<UserRole> userRoles = new HashSet<>();
        Role role1 = new Role();
        role1.setRoleId(1);
        role1.setName("ROLE_USER");
        roleRepository.save(role1);
        userRoles.add(new UserRole(sysUser1, role1));

        userService.createUser(sysUser1, userRoles);


        SysUser sysUser2 = new SysUser();
        sysUser2.setUsername("demo");
        sysUser2.setPassword(SecurityUtility.passwordEncoder().encode("demo"));
        sysUser2.setEmail("565953583@qq.com");
        Set<UserRole> userRoles1 = new HashSet<>();
        Role role2 = new Role();
        role2.setRoleId(1);
        role2.setName("ROLE_USER");
        userRoles1.add(new UserRole(sysUser2, role2));

        userService.createUserOrUpdate(sysUser2, userRoles1);
    }
}

