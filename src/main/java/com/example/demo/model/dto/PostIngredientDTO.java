package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class PostIngredientDTO {

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotNull(message = "Price is mandatory!")
    private BigDecimal price;

}
