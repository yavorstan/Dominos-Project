package com.example.demo.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.MERGE,
            orphanRemoval = true)
    private List<PizzaOrder> pizzaOrders = new ArrayList<>();

    @ManyToOne
    private User user;

    private String comment;

    @OneToOne
    private Address address;

    private LocalDateTime dateAndTimeOfCreation;

}
