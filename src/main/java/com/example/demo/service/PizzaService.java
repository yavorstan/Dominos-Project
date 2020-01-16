package com.example.demo.service;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.model.dto.PostPizzaDTO;
import com.example.demo.model.entity.Pizza;
import com.example.demo.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private IngredientService ingredientService;


    public GetPizzaDTO createPizza(PostPizzaDTO postPizzaDTO) {
        if (pizzaRepository.existsByName(postPizzaDTO.getName())) {
            throw new ElementAlreadyExistsException("Pizza with this name already exists!");
        }
        Pizza pizza = new Pizza();
        pizza.setName(postPizzaDTO.getName());
        pizza.setPrice(postPizzaDTO.getPrice());
        postPizzaDTO
                .getIngredients()
                .stream()
                .map(ingredientId -> ingredientService.findById(ingredientId))
                .forEach(ingredient -> pizza.getIngredients().add(ingredient));
        return pizzaEntityToDTO(pizzaRepository.save(pizza));
    }

    public List<GetPizzaDTO> getMenu() {
        List<GetPizzaDTO> getPizzaResponse = new ArrayList<>();
        pizzaRepository.findAll()
                .stream()
                .map(pizza -> pizzaEntityToDTO(pizza))
                .forEach(getPizzaDTO -> getPizzaResponse.add(getPizzaDTO));
        return Collections.unmodifiableList(getPizzaResponse);
    }

    public GetPizzaDTO updatePizzaPrice(Long id, GetPizzaDTO getPizzaDTO) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Pizza with this ID not found!"));
        BigDecimal newPrice = getPizzaDTO.getPrice();
        if (newPrice == null || newPrice.doubleValue() <= 0){
            throw new ErrorCreatingEntityException("Enter a valid price!");
        }
        pizza.setPrice(getPizzaDTO.getPrice());
        return pizzaEntityToDTO(pizzaRepository.save(pizza));

    }

    public void deletePizzaById(Long id) {
        pizzaRepository.delete(pizzaRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Pizza with this ID not found!")));
    }

    public GetPizzaDTO pizzaEntityToDTO(Pizza pizza) {
        GetPizzaDTO getPizzaDTO = new GetPizzaDTO(pizza.getId(), pizza.getName(), pizza.getPrice());
        getPizzaDTO.setIngredients(new ArrayList<>());
        pizza.getIngredients()
                .stream()
                .map(ingredient -> new GetIngredientDTO(ingredient.getId(), ingredient.getName(), ingredient.getPrice()))
                .forEach(getIngredientDTO -> getPizzaDTO.getIngredients().add(getIngredientDTO));
        return getPizzaDTO;
    }

    public Pizza findById(Long id) {
        return pizzaRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Not pizza with this ID found!"));
    }

}
