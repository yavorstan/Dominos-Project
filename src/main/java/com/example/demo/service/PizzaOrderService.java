package com.example.demo.service;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.model.entity.Pizza;
import com.example.demo.model.entity.PizzaOrder;
import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import com.example.demo.repository.PizzaOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaOrderService {

    @Autowired
    private PizzaOrderRepository pizzaOrderRepository;

    @Autowired
    private PizzaService pizzaService;

    @Autowired
    private IngredientService ingredientService;

    public GetPizzaOptionsDTO getOptions(Long id) throws ElementNotFoundException {
        GetPizzaOptionsDTO getPizzaOptionsDTO = new GetPizzaOptionsDTO();
        Pizza pizza = pizzaService.findById(id);
        getPizzaOptionsDTO.setPizza(pizzaService.pizzaEntityToDTO(pizza));
        getPizzaOptionsDTO.setPizzaSizes(getPizzaSizeDTOS());
        getPizzaOptionsDTO.setPizzaCrusts(getPizzaCrustDTOS());
        getPizzaOptionsDTO.setAllIngredients(ingredientService.getIngredients());
        return getPizzaOptionsDTO;
    }

    public GetPizzaOrderDTO addPizzaOrderToCart(Long pizzaId, PostPizzaOrderDTO postPizzaOrderDTO)
            throws ElementNotFoundException {
        //TODO add to cart
        PizzaOrder pizzaOrder = new PizzaOrder();
        pizzaOrder.setPizza(pizzaService.findById(pizzaId));
        pizzaOrder.setSize(postPizzaOrderDTO.getPizzaSize() == null
                ? PizzaSizeEnum.MEDIUM : postPizzaOrderDTO.getPizzaSize());
        pizzaOrder.setCrust(postPizzaOrderDTO.getPizzaCrust() == null
                ? PizzaCrustEnum.NORMAL : postPizzaOrderDTO.getPizzaCrust());
        pizzaOrder.setAdditionalIngredients(ingredientService.ingredientIdToEntity(postPizzaOrderDTO.getAdditionalIngredientsIds()));
        setFullPrice(pizzaOrder);
        return pizzaOrderEntityToDTO(pizzaOrderRepository.save(pizzaOrder));
    }

    private void setFullPrice(PizzaOrder pizzaOrder) {
        double price = pizzaOrder.getPizza().getPrice();
        price += pizzaOrder.getSize().getAdditionalPrice();
        price += pizzaOrder.getCrust().getAdditionalPrice();
        for (Ingredient ingredient : pizzaOrder.getAdditionalIngredients()){
            price += ingredient.getPrice();
        }
        pizzaOrder.setFullPrice(price);
    }

    private GetPizzaOrderDTO pizzaOrderEntityToDTO(PizzaOrder pizzaOrder){
        GetPizzaOrderDTO getPizzaOrderDTO = new GetPizzaOrderDTO();
        getPizzaOrderDTO.setId(pizzaOrder.getId());
        getPizzaOrderDTO.setOrder(pizzaOrder.getOrder());
        getPizzaOrderDTO.setCart(pizzaOrder.getCart());
        getPizzaOrderDTO.setPizza(pizzaOrder.getPizza());
        getPizzaOrderDTO.setSize(pizzaOrder.getSize());
        getPizzaOrderDTO.setCrust(pizzaOrder.getCrust());
        for (Ingredient ingredient : pizzaOrder.getAdditionalIngredients()){
            getPizzaOrderDTO.getAdditionalIngredients().add(ingredientService.ingredientEntityToDTO(ingredient));
        }
        getPizzaOrderDTO.setFullPrice(pizzaOrder.getFullPrice());
        return getPizzaOrderDTO;
    }

    private List<GetPizzaCrustDTO> getPizzaCrustDTOS() {
        List<GetPizzaCrustDTO> getPizzaCrustDTOS = new ArrayList<>();
        for (PizzaCrustEnum pizzaCrustEnum : PizzaCrustEnum.values()) {
            getPizzaCrustDTOS.add(new GetPizzaCrustDTO(pizzaCrustEnum, pizzaCrustEnum.getAdditionalPrice()));
        }
        return getPizzaCrustDTOS;
    }

    private List<GetPizzaSizeDTO> getPizzaSizeDTOS() {
        List<GetPizzaSizeDTO> getPizzaSizeDTOS = new ArrayList<>();
        for (PizzaSizeEnum pizzaSizeEnum : PizzaSizeEnum.values()) {
            getPizzaSizeDTOS.add(new GetPizzaSizeDTO(pizzaSizeEnum, pizzaSizeEnum.getAdditionalPrice()));
        }
        return getPizzaSizeDTOS;
    }
}
