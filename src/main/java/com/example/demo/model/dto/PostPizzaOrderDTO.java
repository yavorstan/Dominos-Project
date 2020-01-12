package com.example.demo.model.dto;


import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Getter
public class PostPizzaOrderDTO {

    private PizzaSizeEnum size;
    private PizzaCrustEnum crust;
    private List<String> additionalIngredients;

    @Min(1)
    @Max(10)
    private int quantity;

}
