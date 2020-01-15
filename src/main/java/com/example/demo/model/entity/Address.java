package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String city;

    private String phoneNumber;

    private String addressDetails;

    public Address(String city, String phoneNumber, String addressDetails) {
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.addressDetails = addressDetails;
    }
}
