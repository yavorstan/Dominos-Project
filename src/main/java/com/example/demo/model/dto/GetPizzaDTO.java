package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetPizzaDTO {

    private long id;
    private String name;
    private double price;
    private List<GetIngredientDTO> ingredients;

}
