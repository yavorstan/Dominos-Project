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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    private User user;
    @OneToOne
    private City city;
    @Column
    private String phoneNumber;
    @Column
    private String addressDetails;


}
