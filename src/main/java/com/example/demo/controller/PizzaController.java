package com.example.demo.controller;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.PostPizzaDTO;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pizza")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<GetPizzaDTO> createPizza(@Valid @RequestBody PostPizzaDTO postPizzaDTO, Errors errors)
            throws ElementNotFoundException, ElementAlreadyExistsException, ErrorCreatingEntityException {
        //TODO exception handler
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetPizzaDTO getPizzaDTO = pizzaService.createPizza(postPizzaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(getPizzaDTO);
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public List<GetPizzaDTO> getMenu() {
        return pizzaService.getMenu();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updatePizzaPrice(@PathVariable("id") Long id, @RequestBody GetPizzaDTO getPizzaDTO) throws ElementNotFoundException, IllegalArgumentException {
        pizzaService.updatePizzaPrice(id, getPizzaDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePizzaById(@PathVariable("id") Long id) throws ElementNotFoundException {
        pizzaService.deletePizzaById(id);
    }

}