package com.example.demo.controller;

import com.example.demo.model.dto.CreatePizzaDTO;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pizza")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createPizza(@RequestBody CreatePizzaDTO createPizzaDTO) {
        pizzaService.createPizza(createPizzaDTO);
    }

    @GetMapping("/menu")
    public List<GetPizzaDTO> getMenu() {
        return pizzaService.getMenu();
    }

    @PatchMapping
    public void updatePizzaPrice(@RequestBody GetPizzaDTO getPizzaDTO) {
        pizzaService.updatePizzaPrice(getPizzaDTO);
    }

    @DeleteMapping("/delete")
    public void deletePizzaByName(@RequestBody GetPizzaDTO getPizzaDTO) {
        pizzaService.deletePizzaByName(getPizzaDTO);
    }

}