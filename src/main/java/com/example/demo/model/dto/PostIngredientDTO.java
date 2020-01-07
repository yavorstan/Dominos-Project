package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostIngredientDTO {

    public static final int MIN_PRICE_FOR_INGREDIENT = 0;

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @Min(MIN_PRICE_FOR_INGREDIENT)
    private double price;

}
