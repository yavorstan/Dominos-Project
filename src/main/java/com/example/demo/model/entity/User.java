package com.example.demo.model.entity;

import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String first_name;
    private String last_name;
    private String email;
    private String password;
}
