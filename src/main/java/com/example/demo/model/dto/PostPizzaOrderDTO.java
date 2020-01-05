package com.example.demo.model.dto;


import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class PostPizzaOrderDTO {

    private PizzaSizeEnum pizzaSize;
    private PizzaCrustEnum pizzaCrust;
    private List<Long> additionalIngredientsIds;

}
