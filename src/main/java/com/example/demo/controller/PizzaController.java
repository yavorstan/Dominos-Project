package com.example.demo.controller;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
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
    public void createPizza(@RequestBody CreatePizzaDTO createPizzaDTO) throws ElementNotFoundException, ElementAlreadyExistsException {
        pizzaService.createPizza(createPizzaDTO);
    }

    @GetMapping("/menu")
    public List<GetPizzaDTO> getMenu() {
        return pizzaService.getMenu();
    }

    @PatchMapping("/updatePrice")
    public void updatePizzaPrice(@RequestBody GetPizzaDTO getPizzaDTO) throws ElementNotFoundException, IllegalArgumentException {
        pizzaService.updatePizzaPrice(getPizzaDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePizzaById(GetPizzaDTO getPizzaDTO) throws ElementNotFoundException {
        pizzaService.deletePizzaById(getPizzaDTO);
    }

}