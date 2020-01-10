package com.example.demo.controller;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.model.dto.PostPizzaDTO;
import com.example.demo.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pizza")
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @PostMapping
    public ResponseEntity<GetPizzaDTO> createPizza(@Valid @RequestBody PostPizzaDTO postPizzaDTO, Errors errors) {
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetPizzaDTO getPizzaDTO = pizzaService.createPizza(postPizzaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(getPizzaDTO);
    }

    @GetMapping("/menu")
    public List<GetPizzaDTO> getMenu() {
        return pizzaService.getMenu();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetPizzaDTO> updatePizzaPrice(@PathVariable("id") Long id, @RequestBody GetPizzaDTO getPizzaDTO)
            throws ElementNotFoundException, IllegalArgumentException {
        GetPizzaDTO response = pizzaService.updatePizzaPrice(id, getPizzaDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public void deletePizzaById(@PathVariable("id") Long id) {
        pizzaService.deletePizzaById(id);
    }

}