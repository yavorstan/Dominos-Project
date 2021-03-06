package com.example.demo.controller;

import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.model.dto.PostPizzaDTO;
import com.example.demo.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pizza")
public class PizzaController {

    private PizzaService pizzaService;

    private SessionManager sessionManager;

    public PizzaController(SessionManager sessionManager, PizzaService pizzaService) {
        this.sessionManager = sessionManager;
        this.pizzaService = pizzaService;
    }

    @PostMapping
    public ResponseEntity<GetPizzaDTO> createPizza(HttpSession session,
                                                   @Valid @RequestBody PostPizzaDTO postPizzaDTO,
                                                   Errors errors) {
        sessionManager.checkIfAdmin(session);
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetPizzaDTO getPizzaDTO = pizzaService.createPizza(postPizzaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(getPizzaDTO);
    }

    @GetMapping("/menu")
    public List<GetPizzaDTO> getMenu() {
        return pizzaService.getMenu();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetPizzaDTO> updatePizzaPrice(HttpSession session,
                                                        @PathVariable("id") Long id,
                                                        @RequestBody GetPizzaDTO getPizzaDTO) {
        sessionManager.checkIfAdmin(session);
        GetPizzaDTO response = pizzaService.updatePizzaPrice(id, getPizzaDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePizzaById(HttpSession session, @PathVariable("id") Long id) {
        sessionManager.checkIfAdmin(session);
        pizzaService.deletePizzaById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Pizza deletion successful!");
    }

}