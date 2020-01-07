package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPizzaOptionsDTO {

    private GetPizzaDTO pizza;
    private List<GetPizzaSizeDTO> pizzaSizes;
    private List<GetPizzaCrustDTO> pizzaCrusts;
    private List<GetIngredientDTO> allIngredients;

}
