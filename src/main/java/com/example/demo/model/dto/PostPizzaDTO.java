package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PostPizzaDTO {

    private static final int MIN_PRICE_FOR_PIZZA = 0;

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotNull(message = "Price is mandatory!")
    private BigDecimal price;

    @NotEmpty(message = "Pizza must have some ingredients!")
    private List<@NotBlank String> ingredients;
}
