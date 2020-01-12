package com.example.demo.controller;

import com.example.demo.model.dto.GetPizzaOptionsDTO;
import com.example.demo.model.dto.GetPizzaOrderDTO;
import com.example.demo.model.dto.PostPizzaOrderDTO;
import com.example.demo.service.PizzaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/pizza_orders")
public class PizzaOrderController {

    @Autowired
    private PizzaOrderService pizzaOrderService;

    @GetMapping("/{pizzaId}/options")
    public GetPizzaOptionsDTO getPizzaOrderOptions(HttpSession session, @PathVariable("pizzaId") Long id) {
        return pizzaOrderService.getOptions(session, id);
    }

    @PostMapping("/{pizzaId}/addToCart")
    public GetPizzaOrderDTO addPizzaOrderToCart(HttpSession session,
                                                @PathVariable("pizzaId") Long pizzaId,
                                                @RequestBody PostPizzaOrderDTO postPizzaOrderDTO) {
        return pizzaOrderService.addPizzaOrderToCart(session, pizzaId, postPizzaOrderDTO);
    }

    @GetMapping
    public List<GetPizzaOrderDTO> viewPizzaOrders(HttpSession session){
        return pizzaOrderService.viewPizzaOrdersInCart(session);
    }

    @GetMapping("/buy")
    public void buyPizzaOrdersInCart(HttpSession session){
        pizzaOrderService.buyPizzaOrdersInCart(session);
    }

    @DeleteMapping("/{orderId}")
    public void deletePizzaOrder(HttpSession session, @PathVariable("orderId") Long id) {
        pizzaOrderService.deletePizzaOrder(session, id);
    }

}
