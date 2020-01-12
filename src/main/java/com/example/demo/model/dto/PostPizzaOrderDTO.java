package com.example.demo.model.dto;


import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class PostPizzaOrderDTO {

    public static final int DEFAULT_VALUE_FOR_QUANTITY = 1;

    private PizzaSizeEnum size;
    private PizzaCrustEnum crust;
    private List<String> additionalIngredients;

    @Min(value = 1, message = "Must be a positive number!")
    private int quantity = DEFAULT_VALUE_FOR_QUANTITY;

}
