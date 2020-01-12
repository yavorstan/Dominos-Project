package com.example.demo.service;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.PizzaOrder;
import com.example.demo.model.entity.User;
import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import com.example.demo.model.enumeration.TypeOfOrder;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PizzaOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
    private UserService userService;

    public GetPizzaOptionsDTO getOptions(HttpSession session, Long id) {
        userService.checkIfLoggedIn(session);
        GetPizzaDTO getPizzaDTO = pizzaService.pizzaEntityToDTO(pizzaService.findById(id));
        return new GetPizzaOptionsDTO(getPizzaDTO, getPizzaSizeDTOS(), getPizzaCrustDTOS(), ingredientService.getIngredients());
    }

    public GetPizzaOrderDTO addPizzaOrderToCart(HttpSession session, Long pizzaId, PostPizzaOrderDTO postPizzaOrderDTO) {
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
        pizzaOrder.setQuantity(postPizzaOrderDTO.getQuantity());
        pizzaOrder.setFullPrice(calculateFullPrice(pizzaOrder));
        GetPizzaOrderDTO getPizzaOrderDTO = pizzaOrderEntityToDTO(pizzaOrderRepository.save(pizzaOrder));

        List<PizzaOrder> cart = (List<PizzaOrder>) session.getAttribute("cart");
        cart.add(pizzaOrder);

        return getPizzaOrderDTO;
    }

    private BigDecimal calculateFullPrice(PizzaOrder pizzaOrder) {
        BigDecimal price = pizzaOrder.getPizza().getPrice();
        price = price.add(pizzaOrder.getCrust().getAdditionalPrice());
        for (Ingredient ingredient : pizzaOrder.getAdditionalIngredients()) {
            price = price.add(ingredient.getPrice());
        }
        price = price.multiply(pizzaOrder.getSize().getAdditionalPriceMultiplier());
        return price.multiply(new BigDecimal(pizzaOrder.getQuantity())).setScale(2, RoundingMode.UP);
    }

    public List<GetPizzaOrderDTO> viewPizzaOrdersInCart(HttpSession session) {
        userService.checkIfLoggedIn(session);
        List<GetPizzaOrderDTO> getPizzaOrderDTOList = new ArrayList<>();
        checkIfCartIsEmpty(session);
        List<PizzaOrder> cart = (ArrayList<PizzaOrder>) session.getAttribute("cart");
        for (PizzaOrder pizzaOrder : cart) {
            getPizzaOrderDTOList.add(pizzaOrderEntityToDTO(pizzaOrderRepository.findById(pizzaOrder.getId())
                    .orElseThrow(() -> new ElementNotFoundException("No such pizza order!"))));
        }
        return Collections.unmodifiableList(getPizzaOrderDTOList);
    }

    public void buyPizzaOrdersInCart(HttpSession session) {
        userService.checkIfLoggedIn(session);
        checkIfCartIsEmpty(session);
        User user = userService.findByEmail(session);
        Order order = new Order();
        order.setUser(user);
        order.setTypeOfOrder(TypeOfOrder.DELIVERY); // just for testing

        List<PizzaOrder> cart = (ArrayList<PizzaOrder>) session.getAttribute("cart");
        order.setPizzaOrders(cart);
        //TODO fix this
        orderRepository.save(order);
        for (PizzaOrder pizzaOrder : cart){
            pizzaOrderRepository.deleteById(pizzaOrder.getId());
        }
        //TODO add to order and choose delivery type

    }

    public void deletePizzaOrder(HttpSession session, Long id) {
        userService.checkIfLoggedIn(session);
        checkIfCartIsEmpty(session);
        List<PizzaOrder> cart = (ArrayList<PizzaOrder>) session.getAttribute("cart");
        cart.remove(pizzaOrderRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("No such pizza Order")));
        pizzaOrderRepository.delete(pizzaOrderRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("No pizza order with this id!")));
    }

    private GetPizzaOrderDTO pizzaOrderEntityToDTO(PizzaOrder pizzaOrder) {
        List<GetIngredientDTO> getIngredientDTOS = new ArrayList<>();
        pizzaOrder.getAdditionalIngredients()
                .stream()
                .map(ingredient -> ingredientService.ingredientEntityToDTO(ingredient))
                .forEach(getIngredientDTO -> getIngredientDTOS.add(getIngredientDTO));
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

    private void checkIfCartIsEmpty(HttpSession session){
        if (session.getAttribute("cart") == null) {
            throw new ElementNotFoundException("Cart is empty!");
        }
    }
}
