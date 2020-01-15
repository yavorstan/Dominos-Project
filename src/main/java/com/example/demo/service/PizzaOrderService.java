package com.example.demo.service;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.PizzaOrder;
import com.example.demo.model.entity.User;
import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PizzaOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PizzaOrderService {

    @Autowired
    private PizzaOrderRepository pizzaOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PizzaService pizzaService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private AddressRepository addressRepository;

    public GetPizzaOptionsDTO getOptions(Long id) {
        GetPizzaDTO getPizzaDTO = pizzaService.pizzaEntityToDTO(pizzaService.findById(id));
        return new GetPizzaOptionsDTO(getPizzaDTO, getPizzaSizeDTOS(), getPizzaCrustDTOS(), ingredientService.getIngredients());
    }

    /**
     * Here I use a map to return two things from one method to the controller.
     * <p>
     * One is a 'getPizzaOrderDTO', required for the Response Body and is mapped with a key "getPizzaOrderDTO".
     * The other is the ArrayList of PizzaOrders that is then forwarded to the SessionManager so it can
     * set the attribute for a 'cart' in the session. The ArrayList is mapped with key "arrayListPizzaOrders".
     */
    public Map<String, Object> addPizzaOrderToCart(Long pizzaId,
                                                   PostPizzaOrderDTO postPizzaOrderDTO,
                                                   ArrayList<PizzaOrder> cart) {
        PizzaOrder pizzaOrder = new PizzaOrder();
        pizzaOrder.setPizza(pizzaService.findById(pizzaId));
        pizzaOrder.setSize(postPizzaOrderDTO.getSize() == null
                ? PizzaSizeEnum.MEDIUM : postPizzaOrderDTO.getSize());
        pizzaOrder.setCrust(postPizzaOrderDTO.getCrust() == null
                ? PizzaCrustEnum.NORMAL : postPizzaOrderDTO.getCrust());
        if (postPizzaOrderDTO.getAdditionalIngredients() != null) {
            List<Ingredient> additionalIngredientsToAdd = new ArrayList<>();
            postPizzaOrderDTO.getAdditionalIngredients()
                    .forEach(ingredientId -> additionalIngredientsToAdd.add(ingredientService.findById(ingredientId)));
            pizzaOrder.setAdditionalIngredients(additionalIngredientsToAdd);
        }
        pizzaOrder.setQuantity(postPizzaOrderDTO.getQuantity());
        pizzaOrder.setFullPrice(calculateFullPrice(pizzaOrder));
        //here saved it to repository
        GetPizzaOrderDTO getPizzaOrderDTO = pizzaOrderEntityToDTO(pizzaOrder);
        Map<String, Object> mapReturnedToController = new HashMap<>(2);
        mapReturnedToController.put("getPizzaOrderDTO", getPizzaOrderDTO);
        cart.add(pizzaOrder);
        mapReturnedToController.put("arrayListPizzaOrders", cart);
        return Collections.unmodifiableMap(mapReturnedToController);
    }

    private BigDecimal calculateFullPrice(PizzaOrder pizzaOrder) {
        BigDecimal price = pizzaOrder.getPizza().getPrice();
        price = price.add(pizzaOrder.getCrust().getAdditionalPrice());
        if (pizzaOrder.getAdditionalIngredients() != null) {
            for (Ingredient ingredient : pizzaOrder.getAdditionalIngredients()) {
                price = price.add(ingredient.getPrice());
            }
        }
        price = price.multiply(pizzaOrder.getSize().getAdditionalPriceMultiplier());
        return price.multiply(new BigDecimal(pizzaOrder.getQuantity())).setScale(2, RoundingMode.UP);
    }

    public List<GetPizzaOrderDTO> viewPizzaOrdersInCart(ArrayList<PizzaOrder> cart) {
        List<GetPizzaOrderDTO> getPizzaOrderDTOList = new ArrayList<>();
        cart.stream()
                .map(pizzaOrder -> pizzaOrderEntityToDTO(pizzaOrder))
                .forEach(getPizzaOrderDTO -> getPizzaOrderDTOList.add(getPizzaOrderDTO));
        return Collections.unmodifiableList(getPizzaOrderDTOList);
    }

    public void buyPizzaOrdersInCart(User user, ArrayList<PizzaOrder> cart, PostOrderDTO postOrderDTO) {
        Order order = new Order();
        order.setUser(user);
        if (postOrderDTO.getComment() != null) {
            order.setComment(postOrderDTO.getComment());
        } else order.setComment(null);
        order.setAddress(addressRepository.findById(postOrderDTO.getAddress())
                .orElseThrow(() -> new ElementNotFoundException("No address with this ID found!")));
        order.setDateAndTimeOfCreation(LocalDateTime.now());
        orderRepository.save(order);

        for (PizzaOrder pizzaOrder : cart) {
            pizzaOrderRepository.save(pizzaOrder);
            order.getPizzaOrders().add(pizzaOrder);
            pizzaOrder.setOrder(order);
        }
    }

    public void deletePizzaOrder(Long id, ArrayList<PizzaOrder> cart) {
        cart.remove(pizzaOrderRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("No pizza order with this ID found!")));
        pizzaOrderRepository.delete(pizzaOrderRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("No pizza order with this ID found!")));
    }

    private GetPizzaOrderDTO pizzaOrderEntityToDTO(PizzaOrder pizzaOrder) {
        List<GetIngredientDTO> getIngredientDTOS;
        if (pizzaOrder.getAdditionalIngredients() != null) {
            getIngredientDTOS = new ArrayList<>();
            pizzaOrder.getAdditionalIngredients()
                    .stream()
                    .map(ingredient -> ingredientService.ingredientEntityToDTO(ingredient))
                    .forEach(getIngredientDTO -> getIngredientDTOS.add(getIngredientDTO));
        } else getIngredientDTOS = null;

        return new GetPizzaOrderDTO(pizzaOrder.getId(), pizzaOrder.getOrder(), pizzaOrder.getPizza(), pizzaOrder.getSize(),
                pizzaOrder.getCrust(), getIngredientDTOS, pizzaOrder.getQuantity(), pizzaOrder.getFullPrice());
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
