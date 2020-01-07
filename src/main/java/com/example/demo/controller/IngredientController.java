package com.example.demo.controller;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.PostIngredientDTO;
import com.example.demo.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<GetIngredientDTO> createIngredient(@Valid @RequestBody PostIngredientDTO postIngredientDTO,
                                                             Errors errors) {
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetIngredientDTO response = ingredientService.createIngredient(postIngredientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list")
    public List<GetIngredientDTO> getIngredients() {
        return ingredientService.getIngredients();
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable("id") Long id) {
        ingredientService.deleteIngredient(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetIngredientDTO> updateIngredientPrice(@PathVariable("id") Long id,
                                                                  @RequestBody GetIngredientDTO getIngredientDTO) {
        GetIngredientDTO response = ingredientService.updateIngredientPrice(id, getIngredientDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
