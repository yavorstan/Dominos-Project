package com.example.demo.controller;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.GetPizzaOptionsDTO;
import com.example.demo.model.dto.GetPizzaOrderDTO;
import com.example.demo.model.dto.PostPizzaOrderDTO;
import com.example.demo.service.PizzaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pizzaOrder")
public class PizzaOrderController {

    @Autowired
    private PizzaOrderService pizzaOrderService;

    @RequestMapping(value = "/{pizzaId}/options", method = RequestMethod.GET)
    public GetPizzaOptionsDTO getPizzaOrderOptions(@PathVariable("pizzaId") Long id) throws ElementNotFoundException {
        return pizzaOrderService.getOptions(id);
    }

    @RequestMapping(value = "/{pizzaId}/addToCart", method = RequestMethod.POST)
    public GetPizzaOrderDTO addPizzaOrderToCart(@PathVariable("pizzaId") Long pizzaId, @RequestBody PostPizzaOrderDTO postPizzaOrderDTO)
            throws ElementNotFoundException {
        return pizzaOrderService.addPizzaOrderToCart(pizzaId, postPizzaOrderDTO);
    }


}
