package com.example.demo.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreatePizzaDTO {

    private String name;
    private double price;
    private List<String> ingredients;
}
