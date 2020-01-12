package com.example.demo.service;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.PizzaOrder;
import com.example.demo.model.entity.User;
import com.example.demo.model.enumeration.PizzaCrustEnum;
import com.example.demo.model.enumeration.PizzaSizeEnum;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PizzaOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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
        HashMap<Long, Integer> cart = new HashMap<>();
        cart.put(getPizzaOrderDTO.getId(), getPizzaOrderDTO.getQuantity());
        session.setAttribute("cart", cart);

        return getPizzaOrderDTO;
    }

    private BigDecimal calculateFullPrice(PizzaOrder pizzaOrder) {
        BigDecimal price = pizzaOrder.getPizza().getPrice();
        price.add(pizzaOrder.getCrust().getAdditionalPrice());
        for (Ingredient ingredient : pizzaOrder.getAdditionalIngredients()) {
            price.add(ingredient.getPrice());
        }
        price.multiply(pizzaOrder.getSize().getAdditionalPriceMultiplier());
        return price.multiply(new BigDecimal(pizzaOrder.getQuantity()));
    }

    public List<GetPizzaOrderDTO> viewPizzaOrders(HttpSession session){
        userService.checkIfLoggedIn(session);
        List<GetPizzaOrderDTO> getPizzaOrderDTOList = new ArrayList<>();
        HashMap<Long, Integer> cart = (HashMap<Long, Integer>) session.getAttribute("cart");
        for (Long id : cart.keySet()){
            getPizzaOrderDTOList.add(pizzaOrderEntityToDTO(pizzaOrderRepository.findById(id)
                    .orElseThrow(() -> new ElementNotFoundException("No such pizza order!"))));
        }
        return Collections.unmodifiableList(getPizzaOrderDTOList);
    }

    public void buyPizzaOrdersInCart(HttpSession session) {
        userService.checkIfLoggedIn(session);
        User user = userService.findByEmail(session);
        Order order = new Order();
        order.setUser(user);
        //TODO add to order and choose delivery type

    }

    public void deletePizzaOrder(HttpSession session, Long id) {
        userService.checkIfLoggedIn(session);
        HashMap<Long, Integer> cart = (HashMap<Long, Integer>) session.getAttribute("cart");
        cart.remove(id);
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
                pizzaOrder.getCrust(), getIngredientDTOS,pizzaOrder.getQuantity(), pizzaOrder.getFullPrice());
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
