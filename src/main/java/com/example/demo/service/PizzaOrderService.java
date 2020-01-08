package com.example.demo.service;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.model.entity.PizzaOrder;
import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import com.example.demo.repository.PizzaOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PizzaOrderService {

    @Autowired
    private PizzaOrderRepository pizzaOrderRepository;

    @Autowired
    private PizzaService pizzaService;

    @Autowired
    private IngredientService ingredientService;

    public GetPizzaOptionsDTO getOptions(Long id) {
        GetPizzaDTO getPizzaDTO = pizzaService.pizzaEntityToDTO(pizzaService.findById(id));
        return new GetPizzaOptionsDTO(getPizzaDTO, getPizzaSizeDTOS(), getPizzaCrustDTOS(), ingredientService.getIngredients());
    }

    public GetPizzaOrderDTO addPizzaOrderToCart(Long pizzaId, PostPizzaOrderDTO postPizzaOrderDTO) {
        //TODO add to cart
        PizzaOrder pizzaOrder = new PizzaOrder();
        pizzaOrder.setPizza(pizzaService.findById(pizzaId));
        pizzaOrder.setSize(postPizzaOrderDTO.getSize() == null
                ? PizzaSizeEnum.MEDIUM : postPizzaOrderDTO.getSize());
        pizzaOrder.setCrust(postPizzaOrderDTO.getCrust() == null
                ? PizzaCrustEnum.NORMAL : postPizzaOrderDTO.getCrust());
        List<Ingredient> additionalIngredientsToAdd = new ArrayList<>();
        postPizzaOrderDTO.getAdditionalIngredients()
                .forEach(s -> additionalIngredientsToAdd.add(ingredientService.findIngredientByName(s)));
        pizzaOrder.setAdditionalIngredients(additionalIngredientsToAdd);
        pizzaOrder.setFullPrice(calculateFullPrice(pizzaOrder));
        return pizzaOrderEntityToDTO(pizzaOrderRepository.save(pizzaOrder));
    }

    private double calculateFullPrice(PizzaOrder pizzaOrder) {
        double price = pizzaOrder.getPizza().getPrice();
        price += pizzaOrder.getCrust().getAdditionalPrice();
        for (Ingredient ingredient : pizzaOrder.getAdditionalIngredients()) {
            price += ingredient.getPrice();
        }
        price *= pizzaOrder.getSize().getAdditionalPriceMultiplier();
        return price;
    }

    public void deletePizzaOrder(Long id) {
        pizzaOrderRepository.delete(pizzaOrderRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("No pizza order with this id!")));
    }

    private GetPizzaOrderDTO pizzaOrderEntityToDTO(PizzaOrder pizzaOrder) {
        List<GetIngredientDTO> getIngredientDTOS = new ArrayList<>();
        pizzaOrder.getAdditionalIngredients()
                .stream()
                .map(ingredient -> ingredientService.ingredientEntityToDTO(ingredient))
                .forEach(getIngredientDTO -> getIngredientDTOS.add(getIngredientDTO));
        return new GetPizzaOrderDTO(pizzaOrder.getId(), pizzaOrder.getOrder(),
                pizzaOrder.getCart(), pizzaOrder.getPizza(), pizzaOrder.getSize(),
                pizzaOrder.getCrust(), getIngredientDTOS, pizzaOrder.getFullPrice());
    }

    private List<GetPizzaCrustDTO> getPizzaCrustDTOS() {
        List<GetPizzaCrustDTO> getPizzaCrustDTOS = new ArrayList<>();
        Arrays.stream(PizzaCrustEnum.values())
                .map(pizzaCrustEnum -> new GetPizzaCrustDTO(pizzaCrustEnum, pizzaCrustEnum.getAdditionalPrice()))
                .forEach(getPizzaCrustDTO -> getPizzaCrustDTOS.add(getPizzaCrustDTO));
        return getPizzaCrustDTOS;
    }

    private List<GetPizzaSizeDTO> getPizzaSizeDTOS() {
        List<GetPizzaSizeDTO> getPizzaSizeDTOS = new ArrayList<>();
        Arrays.stream(PizzaSizeEnum.values())
                .map(pizzaSizeEnum -> new GetPizzaSizeDTO(pizzaSizeEnum, pizzaSizeEnum.getAdditionalPriceMultiplier()))
                .forEach(getPizzaSizeDTO -> getPizzaSizeDTOS.add(getPizzaSizeDTO));
        return getPizzaSizeDTOS;
    }
}
