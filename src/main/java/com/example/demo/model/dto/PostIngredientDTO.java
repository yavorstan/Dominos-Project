package com.example.demo.model.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostIngredientDTO {

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NonNull
    private double price;

}
