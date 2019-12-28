package com.example.demo.service;

import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.GetPizzaDTO;
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

    public List<GetIngredientDTO> getIngredients() {
        ArrayList<GetIngredientDTO> ingredients = new ArrayList<>();
        GetIngredientDTO getIngredientDTO;
        for (Ingredient i : ingredientRepository.findAll()) {
            getIngredientDTO = new GetIngredientDTO();
            getIngredientDTO.setId(i.getId());
            getIngredientDTO.setName(i.getName());
            getIngredientDTO.setPrice(i.getPrice());
            ingredients.add(getIngredientDTO);
        }
        return Collections.unmodifiableList(ingredients);
    }

    @Transactional
    public void deleteIngredientByName(GetIngredientDTO getIngredientDTO) {
        if (!ingredientRepository.existsByName(getIngredientDTO.getName())) {
            //TODO throw exception
            return;
        }
        ingredientRepository.deleteByName(getIngredientDTO.getName());
    }
}
