package com.example.demo.service;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.model.dto.PostPizzaDTO;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.model.entity.Pizza;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.PizzaRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;
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

    public GetPizzaDTO createPizza(PostPizzaDTO postPizzaDTO) {
        if (pizzaRepository.existsByName(postPizzaDTO.getName())) {
            throw new ElementAlreadyExistsException("Pizza with this name already exists!");
        }
        Pizza pizza = new Pizza();
        pizza.setName(postPizzaDTO.getName());
        pizza.setPrice(postPizzaDTO.getPrice());
        for (String s : postPizzaDTO.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findByName(s.toLowerCase())
                    .orElseThrow(() -> new ElementNotFoundException("No ingredient '" + s + "' found!"));
            pizza.getIngredients().add(ingredient);
        }
        return pizzaEntityToDTO(pizzaRepository.save(pizza));
    }

    public List<GetPizzaDTO> getMenu() {
        List<Pizza> pizzas = pizzaRepository.findAll();
        List<GetPizzaDTO> getPizzaResponse = new ArrayList<>();
        for (Pizza pizza : pizzas) {
            GetPizzaDTO getPizzaDTO = pizzaEntityToDTO(pizza);
            getPizzaResponse.add(getPizzaDTO);
        }
        return Collections.unmodifiableList(getPizzaResponse);
    }

    public GetPizzaDTO updatePizzaPrice(Long id, GetPizzaDTO getPizzaDTO) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Pizza with this ID not found!"));
        if (getPizzaDTO.getPrice() > 0) {
            pizza.setPrice(getPizzaDTO.getPrice());
        }
        if (getPizzaDTO.getName() != null){
            pizza.setName(getPizzaDTO.getName());
        }
        return pizzaEntityToDTO(pizzaRepository.save(pizza));
    }

    public void deletePizzaById(Long id) {
        pizzaRepository.delete(pizzaRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Pizza with this ID not found!")));
    }

    public GetPizzaDTO pizzaEntityToDTO(Pizza pizza) {
        GetPizzaDTO getPizzaDTO = new GetPizzaDTO();
        getPizzaDTO.setId(pizza.getId());
        getPizzaDTO.setName(pizza.getName());
        getPizzaDTO.setPrice(pizza.getPrice());
        List<GetIngredientDTO> list = new ArrayList<>();
        getPizzaDTO.setIngredients(list);
        for (Ingredient ingredient : pizza.getIngredients()) {
            GetIngredientDTO getIngredientDTO = new GetIngredientDTO();
            getIngredientDTO.setId(ingredient.getId());
            getIngredientDTO.setName(ingredient.getName());
            getIngredientDTO.setPrice(ingredient.getPrice());
            getPizzaDTO.getIngredients().add(getIngredientDTO);
        }
        return getPizzaDTO;
    }

    public Pizza findById(Long id) {
        return pizzaRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Not pizza with this ID found!"));
    }

}
