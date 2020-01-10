package com.example.demo.model.dto;

import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class GetPizzaSizeDTO {

    private PizzaSizeEnum pizzaSize;
    private BigDecimal price;
}
