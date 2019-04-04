package com.bookstore.service;

import com.bookstore.domain.UserShipping;
import org.springframework.stereotype.Service;

@Service
public interface UserShippingService {

    UserShipping findById(Long id);
    void removeById(Long id);

}
