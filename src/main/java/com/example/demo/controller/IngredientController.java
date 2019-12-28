package com.example.demo.controller;

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

    @DeleteMapping("/delete")
    public void deletePizzaByName(@RequestBody GetIngredientDTO getIngredientDTO) {
        ingredientService.deleteIngredientByName(getIngredientDTO);
    }

}
