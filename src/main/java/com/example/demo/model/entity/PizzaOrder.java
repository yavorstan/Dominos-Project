package com.example.demo.model.entity;

import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
public class PizzaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //TODO link to order

    @OneToOne
    private Pizza pizza;
    //default values for those components
    @NotBlank
    private PizzaSizeEnum size;
    @NotBlank
    private PizzaCrustEnum crust;

}
