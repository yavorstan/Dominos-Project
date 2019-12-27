package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetPizzaDTO {

    private Long id;
    private String name;
    private List<GetIngredientDTO> ingredients;

}
