package com.example.demo.model.dto;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class PostPizzaDTO {

    private static final int MIN_PRICE_FOR_PIZZA = 0;

    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotNull(message = "Price is mandatory!")
    @Min(MIN_PRICE_FOR_PIZZA)
    private double price;

    @NotEmpty(message = "Pizza must have some ingredients!")
    private List<String> ingredients;
}
