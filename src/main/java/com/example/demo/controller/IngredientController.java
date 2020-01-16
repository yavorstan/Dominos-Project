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

    private IngredientService ingredientService;

    private SessionManager sessionManager;

    public IngredientController(SessionManager sessionManager, IngredientService ingredientService) {
        this.sessionManager = sessionManager;
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<GetIngredientDTO> createIngredient(HttpSession session,
                                                             @Valid @RequestBody PostIngredientDTO postIngredientDTO,
                                                             Errors errors) {
        sessionManager.checkIfAdmin(session);
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetIngredientDTO response = ingredientService.createIngredient(postIngredientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list")
    public List<GetIngredientDTO> getIngredients(HttpSession session) {
        sessionManager.checkIfLoggedIn(session);
        sessionManager.checkIfAdmin(session);
        return ingredientService.getIngredients();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredient(HttpSession session, @PathVariable("id") Long id) {
        sessionManager.checkIfAdmin(session);
        ingredientService.deleteIngredient(id);
        return ResponseEntity.status(HttpStatus.OK).body("Ingredient deletion successful!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetIngredientDTO> updateIngredientPrice(HttpSession session,
                                                                  @PathVariable("id") Long id,
                                                                  @RequestBody PostIngredientDTO postIngredientDTO) {
        sessionManager.checkIfAdmin(session);
        GetIngredientDTO response = ingredientService.updateIngredientPrice(id, postIngredientDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
