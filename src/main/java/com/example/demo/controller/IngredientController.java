package com.example.demo.controller;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    IngredientService ingredientService;

    @GetMapping("/list")
    public List<GetIngredientDTO> getIngredients() {
        return ingredientService.getIngredients();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteIngredient(GetIngredientDTO getIngredientDTO) throws ElementNotFoundException {
        ingredientService.deleteIngredient(getIngredientDTO);
    }

    @PatchMapping("/update")
    public void updateIngredientPrice(@RequestBody GetIngredientDTO getIngredientDTO) throws ElementNotFoundException {
        ingredientService.updateIngredientPrice(getIngredientDTO);
    }

}
