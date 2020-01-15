package com.example.demo.model.entity;

import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
@EqualsAndHashCode
public class PizzaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @OneToOne
    private Pizza pizza;

    @Enumerated
    private PizzaSizeEnum size;

    @Enumerated
    private PizzaCrustEnum crust;

    @ManyToMany
    private List<Ingredient> additionalIngredients;

    private int quantity;

    private BigDecimal fullPrice;

}
