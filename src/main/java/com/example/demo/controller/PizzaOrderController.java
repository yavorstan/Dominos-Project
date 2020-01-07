package com.example.demo.controller;

import com.example.demo.model.dto.GetPizzaOptionsDTO;
import com.example.demo.model.dto.GetPizzaOrderDTO;
import com.example.demo.model.dto.PostPizzaOrderDTO;
import com.example.demo.service.PizzaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pizza_orders")
public class PizzaOrderController {

    @Autowired
    private PizzaOrderService pizzaOrderService;

    @GetMapping("/{pizzaId}/options")
    public GetPizzaOptionsDTO getPizzaOrderOptions(@PathVariable("pizzaId") Long id) {
        return pizzaOrderService.getOptions(id);
    }

    @PostMapping("/{pizzaId}/addToCart")
    public GetPizzaOrderDTO addPizzaOrderToCart(@PathVariable("pizzaId") Long pizzaId,
                                                @RequestBody PostPizzaOrderDTO postPizzaOrderDTO) {
        return pizzaOrderService.addPizzaOrderToCart(pizzaId, postPizzaOrderDTO);
    }

    @DeleteMapping("/{orderId}")
    public void deletePizzaOrder(@PathVariable("orderId") Long id) {
        pizzaOrderService.deletePizzaOrder(id);
    }

}
