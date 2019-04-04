package com.bookstore.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String shippingAddressName;
    private String shippingAddressStreet1;
    private String shippingAddressStreet2;
    private String shippingAddressCity;
    private String shippingAddressState;
    private String shippingAddressCountry;
    private String shippingAddressZipcode;


    @OneToOne
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SysUser sysUser;
}
