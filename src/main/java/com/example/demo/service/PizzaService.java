package com.example.demo.service;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.CreatePizzaDTO;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.model.entity.Pizza;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public void createPizza(CreatePizzaDTO createPizzaDTO) throws ElementAlreadyExistsException, ElementNotFoundException {
        if (pizzaRepository.existsByName(createPizzaDTO.getName())) {
            throw new ElementAlreadyExistsException("Pizza with this name already exists!");
        }
        Pizza pizza = new Pizza();
        pizza.setName(createPizzaDTO.getName());
        pizza.setPrice(createPizzaDTO.getPrice());
        for (String s : createPizzaDTO.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findByName(s.toLowerCase())
                    .orElseThrow(() -> new ElementNotFoundException("No ingredient '" + s + "' found!"));
            pizza.getIngredients().add(ingredient);
        }
        pizzaRepository.save(pizza);
    }

    public List<GetPizzaDTO> getMenu() {
        List<Pizza> pizzas = pizzaRepository.findAll();
        List<GetPizzaDTO> getPizzaResponse = new ArrayList<>();
        for (Pizza pizza : pizzas) {
            GetPizzaDTO getPizzaDTO = new GetPizzaDTO();
            getPizzaDTO.setId(pizza.getId());
            getPizzaDTO.setName(pizza.getName());
            getPizzaDTO.setPrice(pizza.getPrice());
            List<Ingredient> list = new ArrayList<>();
            getPizzaDTO.setIngredients(list);
            for (Ingredient ingredient : pizza.getIngredients()) {
                GetIngredientDTO getIngredientDTO = new GetIngredientDTO();
                getIngredientDTO.setId(ingredient.getId());
                getIngredientDTO.setName(ingredient.getName());
                getPizzaDTO.getIngredients().add(ingredient);
            }
            getPizzaResponse.add(getPizzaDTO);
        }
        return Collections.unmodifiableList(getPizzaResponse);
    }

    public void deletePizzaById(Long id) throws ElementNotFoundException {
        pizzaRepository.delete(pizzaRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Pizza with this ID not found!")));
    }

    public void updatePizzaPrice(Long id, GetPizzaDTO getPizzaDTO) throws ElementNotFoundException, IllegalArgumentException {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Pizza with this ID not found!"));
        if (getPizzaDTO.getPrice() <= 0) {
            throw new IllegalArgumentException("Illegal price tag!");
        }
        pizza.setPrice(getPizzaDTO.getPrice());
        pizzaRepository.save(pizza);
    }

}
