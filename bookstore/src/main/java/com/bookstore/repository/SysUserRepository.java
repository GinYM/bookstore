package com.bookstore.repository;

import com.bookstore.domain.SysUser;
import org.springframework.data.repository.CrudRepository;

public interface SysUserRepository extends CrudRepository<SysUser, Long> {
    SysUser findByUsername(String username);
    SysUser findByEmail(String email);
}
