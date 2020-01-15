package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetPizzaDTO {

    private long id;
    private String name;
    @Min(value = 1, message = "Must be positive price!")
    private BigDecimal price;
    private List<GetIngredientDTO> ingredients;

    public GetPizzaDTO(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
