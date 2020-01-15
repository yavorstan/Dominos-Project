package com.example.demo.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PostOrderDTO {

    private String comment;

    @NotNull(message = "An order must have a delivery address!")
    private Long address;

}
