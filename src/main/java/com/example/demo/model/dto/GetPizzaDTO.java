package com.example.demo.model.dto;

import com.example.demo.model.entity.Ingredient;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetPizzaDTO {

    private long id;
    private String name;
    private double price;
    private List<Ingredient> ingredients;

}
