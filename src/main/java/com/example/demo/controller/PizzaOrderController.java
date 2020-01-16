package com.example.demo.controller;

import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.GetPizzaOptionsDTO;
import com.example.demo.model.dto.GetPizzaOrderDTO;
import com.example.demo.model.dto.PostOrderDTO;
import com.example.demo.model.dto.PostPizzaOrderDTO;
import com.example.demo.model.entity.PizzaOrder;
import com.example.demo.model.entity.User;
import com.example.demo.service.PizzaOrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pizza_orders")
public class PizzaOrderController {

    private PizzaOrderService pizzaOrderService;

    private SessionManager sessionManager;

    public PizzaOrderController(SessionManager sessionManager, PizzaOrderService pizzaOrderService){
        this.sessionManager = sessionManager;
        this.pizzaOrderService = pizzaOrderService;
    }

    @GetMapping("/{pizzaId}/options")
    public ResponseEntity<GetPizzaOptionsDTO> getPizzaOrderOptions(HttpSession session, @PathVariable("pizzaId") Long id) {
        sessionManager.checkIfLoggedIn(session);
        GetPizzaOptionsDTO getPizzaOptionsDTO = pizzaOrderService.getOptions(id);
        return ResponseEntity.status(HttpStatus.OK).body(getPizzaOptionsDTO);
    }

    @PostMapping("/{pizzaId}/add_to_cart")
    public ResponseEntity<GetPizzaOrderDTO> addPizzaOrderToCart(HttpSession session,
                                                                @PathVariable("pizzaId") Long pizzaId,
                                                                @RequestBody PostPizzaOrderDTO postPizzaOrderDTO,
                                                                Errors errors) {
        if (postPizzaOrderDTO.getQuantity() < 1) {
            throw new ErrorCreatingEntityException("Invalid quantity number!");
        }
        sessionManager.checkIfLoggedIn(session);
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        ArrayList<PizzaOrder> cart = sessionManager.getCartAttribute(session);
        Map<String, Object> returnedCollection = pizzaOrderService.addPizzaOrderToCart(pizzaId, postPizzaOrderDTO, cart);
        GetPizzaOrderDTO getPizzaOrderDTO = (GetPizzaOrderDTO) returnedCollection.get("getPizzaOrderDTO");
        sessionManager.setCartAttribute(session, (ArrayList<PizzaOrder>) returnedCollection.get("arrayListPizzaOrders"));
        return ResponseEntity.status(HttpStatus.OK).body(getPizzaOrderDTO);
    }

    @GetMapping
    public List<GetPizzaOrderDTO> viewPizzaOrdersInCart(HttpSession session) {
        sessionManager.checkIfCartIsEmpty(session);
        return pizzaOrderService.viewPizzaOrdersInCart(sessionManager.getCartAttribute(session));
    }

    @GetMapping("/buy")
    @Transactional
    public ResponseEntity<String> buyPizzaOrdersInCart(HttpSession session,
                                                       @Valid @RequestBody PostOrderDTO postOrderDTO,
                                                       Errors errors) {
        sessionManager.checkIfCartIsEmpty(session);
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        ArrayList<PizzaOrder> cart = sessionManager.getCartAttribute(session);
        User user = sessionManager.findUserByEmail(session);
        pizzaOrderService.buyPizzaOrdersInCart(user, cart, postOrderDTO);
        sessionManager.emptyCart(session);
        return ResponseEntity.status(HttpStatus.OK).body("Thank you for your order!");
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deletePizzaOrderFromCart(HttpSession session, @PathVariable("orderId") Long id) {
        sessionManager.checkIfCartIsEmpty(session);
        ArrayList<PizzaOrder> cart = pizzaOrderService.deletePizzaOrder(id, sessionManager.getCartAttribute(session));
        sessionManager.setCartAttribute(session, cart);
        return ResponseEntity.status(HttpStatus.OK).body("Deletion from cart successful!");
    }

}
