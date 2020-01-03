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

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createIngredient(@Valid @RequestBody PostIngredientDTO postIngredientDTO, Errors errors)
            throws ErrorCreatingEntityException, ElementAlreadyExistsException {
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException("Error");
        }
        ingredientService.createIngredient(postIngredientDTO);
        //TODO return ingredientDTO
        return ResponseEntity.status(HttpStatus.CREATED).body(postIngredientDTO);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<GetIngredientDTO> getIngredients() {
        return ingredientService.getIngredients();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteIngredient(@PathVariable("id") Long id) throws ElementNotFoundException {
        ingredientService.deleteIngredient(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateIngredientPrice(@PathVariable("id") Long id, @RequestBody GetIngredientDTO getIngredientDTO) throws ElementNotFoundException {
        ingredientService.updateIngredientPrice(id, getIngredientDTO);
    }

}
