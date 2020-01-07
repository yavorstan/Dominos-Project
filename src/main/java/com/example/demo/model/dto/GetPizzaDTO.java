package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetPizzaDTO {

    private long id;
    private String name;
    private double price;
    private List<GetIngredientDTO> ingredients;

    public GetPizzaDTO(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
