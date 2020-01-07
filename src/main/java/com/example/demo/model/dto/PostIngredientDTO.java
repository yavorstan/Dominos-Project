package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PostIngredientDTO {

    public static final int MIN_PRICE_FOR_INGREDIENT = 0;

    @NotNull(message = "Name is mandatory!")
    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotNull(message = "Price is mandatory!")
    @Min(MIN_PRICE_FOR_INGREDIENT)
    private double price;

}
