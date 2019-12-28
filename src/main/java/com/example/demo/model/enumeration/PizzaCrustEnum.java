package com.example.demo.model.enumeration;

public enum PizzaCrustEnum {

    //sample
    NORMAL(1),
    ITALIAN(1.3),
    PHILADELPHIA(1.7);

    private double priceMultiplier;

    PizzaCrustEnum(double priceMultiplier){
        this.priceMultiplier = priceMultiplier;
    }
}
