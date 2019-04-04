package com.bookstore.saver.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserBilling {

    @Id
    @Column(name="b_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bId;
    private String userBillingName;
    private String userBillingStreet1;
    private String userBillingStreet2;
    private String userBillingCity;
    private String userBillingState;
    private String userBillingCountry;
    private String userBillingZipcode;

    @OneToOne(cascade = CascadeType.ALL)
    private UserPayment userPayment;
}
