package com.example.demo.service;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;

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
    public void deleteIngredient(GetIngredientDTO getIngredientDTO) throws ElementNotFoundException {
        ingredientRepository.delete(ingredientRepository.findById(getIngredientDTO.getId())
                .orElseThrow(() -> new ElementNotFoundException("No ingredient with this ID found!")));
    }


    public void updateIngredientPrice(GetIngredientDTO getIngredientDTO) throws ElementNotFoundException {
        Ingredient ingredient = ingredientRepository.findByName(getIngredientDTO.getName()).
                orElseThrow(() -> new ElementNotFoundException("Not ingredient with this name found!"));
        ingredient.setPrice(getIngredientDTO.getPrice());
        ingredientRepository.save(ingredient);
    }
}
