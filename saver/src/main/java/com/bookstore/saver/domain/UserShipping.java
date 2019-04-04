package com.bookstore.saver.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserShipping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userShippingName;
    private String userShippingStreet1;
    private String userShippingStreet2;
    private String userShippingCity;
    private String userShippingState;
    private String userShippingCountry;
    private String userShippingZipCode;
    private boolean userShippingDefault;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private SysUser sysUser;
}
