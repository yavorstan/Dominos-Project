package com.example.demo.model.dto;


import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class PostPizzaOrderDTO {

    private PizzaSizeEnum size;
    private PizzaCrustEnum crust;
    private List<String> additionalIngredients;

}
