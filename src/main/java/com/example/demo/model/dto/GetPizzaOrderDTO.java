package com.example.demo.model.dto;

import com.example.demo.model.entity.Cart;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.Pizza;
import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPizzaOrderDTO {

    private long id;
    private Order order;
    private Cart cart;
    private Pizza pizza;
    private PizzaSizeEnum size;
    private PizzaCrustEnum crust;
    private List<GetIngredientDTO> additionalIngredients = new ArrayList<>();
    private BigDecimal fullPrice;

}
