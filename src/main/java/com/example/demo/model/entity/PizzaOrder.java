package com.example.demo.model.entity;

import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@EqualsAndHashCode
public class PizzaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @OneToOne
    private Pizza pizza;

    @Enumerated
    private PizzaSizeEnum size;

    @Enumerated
    private PizzaCrustEnum crust;

    @ManyToMany
    private List<Ingredient> additionalIngredients;

    private double fullPrice;

}
