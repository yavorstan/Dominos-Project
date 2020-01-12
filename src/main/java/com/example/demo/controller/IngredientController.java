package com.example.demo.controller;

import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.PostIngredientDTO;
import com.example.demo.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<GetIngredientDTO> createIngredient(HttpSession session,
                                                             @Valid @RequestBody PostIngredientDTO postIngredientDTO,
                                                             Errors errors) {
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetIngredientDTO response = ingredientService.createIngredient(session, postIngredientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list")
    public List<GetIngredientDTO> getIngredients() {
        return ingredientService.getIngredients();
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(HttpSession session, @PathVariable("id") Long id) {
        ingredientService.deleteIngredient(session, id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetIngredientDTO> updateIngredientPrice(HttpSession session,
                                                                  @PathVariable("id") Long id,
                                                                  @RequestBody PostIngredientDTO postIngredientDTO) {
        GetIngredientDTO response = ingredientService.updateIngredientPrice(session, id, postIngredientDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
