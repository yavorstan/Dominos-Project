package com.example.demo.model.dto;

import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
public class PostPizzaDTO {

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NonNull
    private double price;

    @NotEmpty(message = "Pizza must have some ingredients")
    private List<String> ingredients;
}
