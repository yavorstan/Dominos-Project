package com.example.demo.model.entity;

import com.example.demo.model.enumeration.TypeOfOrder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.MERGE,
            orphanRemoval = true)
    private List<PizzaOrder> pizzaOrders = new ArrayList<>();

    @ManyToOne
    private User user;

    @Enumerated
    private TypeOfOrder typeOfOrder;

}
