package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
public class Restaurant {
    private long id;
    private City city;
    private String name;
    private String streetName;
    private int streetNumber;
}
