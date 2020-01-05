package com.example.demo.model.dto;

import com.example.demo.model.entity.Cart;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.Pizza;
import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetPizzaOrderDTO {

    private long id;
    private Order order;
    private Cart cart;
    private Pizza pizza;
    private PizzaSizeEnum size;
    private PizzaCrustEnum crust;
    private List<GetIngredientDTO> additionalIngredients = new ArrayList<>();
    private double fullPrice;

}
