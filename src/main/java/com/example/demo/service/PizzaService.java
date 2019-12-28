package com.example.demo.service;

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

    public void createPizza(CreatePizzaDTO createPizzaDTO) {
        if (pizzaRepository.existsByName(createPizzaDTO.getName())) {
            //TODO should throw something like "PizzaAlreadyExistsException"
            return;
        }
        Pizza pizza = new Pizza();
        pizza.setName(createPizzaDTO.getName());
        pizza.setPrice(createPizzaDTO.getPrice());
        for (String s : createPizzaDTO.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            if (ingredientRepository.existsByName(s)) {
                ingredient = ingredientRepository.findByName(s.toLowerCase());
            } else {
                ingredient.setName(s.toLowerCase());
                ingredientRepository.save(ingredient);
            }
            pizza.getIngredients().add(ingredient);
        }
        pizzaRepository.save(pizza);
    }

    public List<GetPizzaDTO> getMenu() {
        List<Pizza> pizzas = pizzaRepository.findAll();
        List<GetPizzaDTO> getPizzaResponse = new ArrayList<>();
        for (Pizza p : pizzas) {
            GetPizzaDTO getPizzaDTO = new GetPizzaDTO();
            getPizzaDTO.setId(p.getId());
            getPizzaDTO.setName(p.getName());
            getPizzaDTO.setPrice(p.getPrice());
            List<GetIngredientDTO> list = new ArrayList<>();
            getPizzaDTO.setIngredients(list);
            for (Ingredient i : p.getIngredients()) {
                GetIngredientDTO getIngredientDTO = new GetIngredientDTO();
                getIngredientDTO.setId(i.getId());
                getIngredientDTO.setName(i.getName());
                getPizzaDTO.getIngredients().add(getIngredientDTO);
            }
            getPizzaResponse.add(getPizzaDTO);
        }
        return Collections.unmodifiableList(getPizzaResponse);
    }

    public void deletePizzaByName(GetPizzaDTO getPizzaDTO) {
        if (!pizzaRepository.existsByName(getPizzaDTO.getName())) {
            //TODO throw PizzaDoesntExistException
            return;
        }
        Pizza p = pizzaRepository.findByName(getPizzaDTO.getName());
        pizzaRepository.delete(p);
    }

    public void updatePizzaPrice(GetPizzaDTO getPizzaDTO) {
        if (!pizzaRepository.existsByName(getPizzaDTO.getName())) {
            //TODO throw exception
            return;
        }
        Pizza p = pizzaRepository.findByName(getPizzaDTO.getName());
        p.setPrice(getPizzaDTO.getPrice());
        pizzaRepository.save(p);
    }
}
