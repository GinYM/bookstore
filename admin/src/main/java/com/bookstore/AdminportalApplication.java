package com.bookstore;

import com.bookstore.domain.SysUser;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.UserService;
import com.bookstore.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableCaching
public class AdminportalApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(AdminportalApplication.class, args);
    }

    @Override
    public void run(String ... args) throws Exception {
        SysUser sysUser1 = new SysUser();
        sysUser1.setUsername("admin");
        sysUser1.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
        sysUser1.setEmail("admin@gmail.com");
        Set<UserRole> userRoles = new HashSet<>();
        Role role1 = new Role();
        role1.setRoleId(2);
        role1.setName("ROLE_ADMIN");
        userRoles.add(new UserRole(sysUser1, role1));

        userService.createUser(sysUser1, userRoles);
    }
}

//@Configuration
//class RestTemplateConfig {
//
//    // Create a bean for restTemplate to call services
//    @Bean
//    @LoadBalanced        // Load balance between service instances running at different ports.
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//}
