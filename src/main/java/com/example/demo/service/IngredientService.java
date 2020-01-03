package com.example.demo.service;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.PostIngredientDTO;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;

    public void createIngredient(PostIngredientDTO postIngredientDTO) throws ElementAlreadyExistsException {
        if (ingredientRepository.existsByName(postIngredientDTO.getName())) {
            throw new ElementAlreadyExistsException("There is an ingredient with this name!");
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setName(postIngredientDTO.getName());
        ingredient.setPrice(postIngredientDTO.getPrice());
        ingredientRepository.save(ingredient);
    }

    public List<GetIngredientDTO> getIngredients() {
        ArrayList<GetIngredientDTO> ingredients = new ArrayList<>();
        GetIngredientDTO getIngredientDTO;
        for (Ingredient ingredient : ingredientRepository.findAll()) {
            getIngredientDTO = new GetIngredientDTO();
            getIngredientDTO.setId(ingredient.getId());
            getIngredientDTO.setName(ingredient.getName());
            getIngredientDTO.setPrice(ingredient.getPrice());
            ingredients.add(getIngredientDTO);
        }
        return Collections.unmodifiableList(ingredients);
    }

    @Transactional
    public void deleteIngredient(Long id) throws ElementNotFoundException {
        ingredientRepository.delete(ingredientRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("No ingredient with this ID found!")));
    }

    public void updateIngredientPrice(Long id, GetIngredientDTO getIngredientDTO) throws ElementNotFoundException {
        Ingredient ingredient = ingredientRepository.findById(id).
                orElseThrow(() -> new ElementNotFoundException("Not ingredient with this ID found!"));
        ingredient.setPrice(getIngredientDTO.getPrice());
        ingredientRepository.save(ingredient);
    }

}
