package com.example.demo.service;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.UnauthorizedAccessException;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.PostIngredientDTO;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UserService userService;

    public Ingredient findIngredientByName(String name) {
        return ingredientRepository.findByName(name)
                .orElseThrow(() -> new ElementNotFoundException("No ingredient with this name found!"));
    }

    public GetIngredientDTO createIngredient(HttpSession session, PostIngredientDTO postIngredientDTO) {
        userService.checkIfAdmin(session);
        if (ingredientRepository.existsByName(postIngredientDTO.getName())) {
            throw new ElementAlreadyExistsException("There is an ingredient with this name!");
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setName(postIngredientDTO.getName());
        ingredient.setPrice(postIngredientDTO.getPrice());
        return ingredientEntityToDTO(ingredientRepository.save(ingredient));
    }

    public List<GetIngredientDTO> getIngredients() {
        ArrayList<GetIngredientDTO> ingredients = new ArrayList<>();
        ingredientRepository.findAll()
                .forEach(ingredient -> ingredients.add(ingredientEntityToDTO(ingredient)));
        return Collections.unmodifiableList(ingredients);
    }

    public GetIngredientDTO ingredientEntityToDTO(Ingredient ingredient) {
        return new GetIngredientDTO(ingredient.getId(), ingredient.getName(), ingredient.getPrice());
    }

    public void deleteIngredient(HttpSession session, Long id) {
        userService.checkIfAdmin(session);
        ingredientRepository.delete(ingredientRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("No ingredient with this ID found!")));
    }

    public GetIngredientDTO updateIngredientPrice(HttpSession session, Long id, PostIngredientDTO postIngredientDTO) {
        userService.checkIfAdmin(session);
        Ingredient ingredient = ingredientRepository.findById(id).
                orElseThrow(() -> new ElementNotFoundException("Not ingredient with this ID found!"));
        ingredient.setPrice(postIngredientDTO.getPrice());
        return ingredientEntityToDTO(ingredientRepository.save(ingredient));
    }

}
