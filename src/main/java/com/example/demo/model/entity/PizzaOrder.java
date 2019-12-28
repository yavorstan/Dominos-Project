package com.example.demo.model.entity;

import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Setter
public class PizzaOrder {

    @Id
    @GeneratedValue
    private long id;

    //TODO link to order

    @OneToOne
    private Pizza pizza;
    //default values for those components
    @NonNull
    private PizzaSizeEnum size = PizzaSizeEnum.MEDIUM;
    @NonNull
    private PizzaCrustEnum crust = PizzaCrustEnum.NORMAL;


}
