package com.example.demo.service;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Address;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PizzaOrderService pizzaOrderService;

    @Autowired
    private OrderRepository orderRepository;

    public GetUserDTO register(RegisterUserDTO registerUserDTO) {
        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new ElementAlreadyExistsException("There is another user with this email address!");
        }
        if (!registerUserDTO.getPassword().equals(registerUserDTO.getRepeatPassword())) {
            throw new ErrorCreatingEntityException("Passwords do not match!");
        }
        User user = new User(
                registerUserDTO.getFirstName(),
                registerUserDTO.getLastName(),
                registerUserDTO.getEmail(),
                passwordEncoder().encode(registerUserDTO.getPassword()));
        userRepository.save(user);
        return userEntityToDTO(user);
    }

    public GetUserDTO loginUser(LoginUserDTO loginUserDTO) {
        User user = userRepository.findByEmail(loginUserDTO.getEmail())
                .orElseThrow(() -> new ElementNotFoundException("Invalid email and/or password!"));
        if (passwordEncoder().matches(loginUserDTO.getPassword(), user.getPassword())) {
            return userEntityToDTO(user);
        } else throw new ElementNotFoundException("Invalid email and/or password!");
    }

    public GetAddressDTO addNewAddress(User user, PostAddressDTO postAddressDTO) {
        Address address = new Address(postAddressDTO.getCity(), postAddressDTO.getPhoneNumber(), postAddressDTO.getAddressDetails());
        address.setUser(user);
        addressRepository.save(address);
        GetAddressDTO getAddressDTO = new GetAddressDTO(
                address.getId(),
                address.getCity(),
                address.getPhoneNumber(),
                address.getAddressDetails());
        user.getAddresses().add(address);
        return getAddressDTO;
    }

    public List<GetAddressDTO> getAllAddresses(User user) {
        List<GetAddressDTO> getAddressDTOList = new ArrayList<>();
        user.getAddresses().stream()
                .map(address -> new GetAddressDTO(address.getId(), address.getCity(), address.getPhoneNumber(), address.getAddressDetails()))
                .forEach(getAddressDTO -> getAddressDTOList.add(getAddressDTO));
        return Collections.unmodifiableList(getAddressDTOList);
    }

    public List<GetOrderDTO> getAllOrders(User user) {
        if (user.getOrders().isEmpty()) {
            throw new ElementNotFoundException("You have no past orders!");
        }
        ArrayList<Order> orders = orderRepository.findAllByUser(user);
        List<GetOrderDTO> getOrderDTOList = new ArrayList<>();
        orders.stream()
                .map(order -> orderEntityToDTO(order))
                .forEach(getOrderDTO -> getOrderDTOList.add(getOrderDTO));
        return Collections.unmodifiableList(getOrderDTOList);
    }

    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new ElementNotFoundException("No such address found!");
        }
        addressRepository.deleteById(id);
    }

    public GetUserDTO userEntityToDTO(User user) {
        return new GetUserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public GetOrderDTO orderEntityToDTO(Order order) {
        GetOrderDTO getOrderDTO = new GetOrderDTO();
        getOrderDTO.setId(order.getId());

        List<GetPizzaOrderDTO> getPizzaOrderDTOList = new ArrayList<>();
        order.getPizzaOrders().stream()
                .map(pizzaOrder -> pizzaOrderService.pizzaOrderEntityToDTO(pizzaOrder))
                .forEach(getPizzaOrderDTO -> getPizzaOrderDTOList.add(getPizzaOrderDTO));
        getOrderDTO.setPizzaOrders(getPizzaOrderDTOList);
        getOrderDTO.setAddress(addressEntityToDTO(order.getAddress()));
        getOrderDTO.setComment(order.getComment());
        getOrderDTO.setDateAndTimeOfCreation(order.getDateAndTimeOfCreation());
        return getOrderDTO;
    }

    public GetAddressDTO addressEntityToDTO(Address address) {
        return new GetAddressDTO(address.getId(), address.getCity(), address.getPhoneNumber(), address.getAddressDetails());
    }

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ElementNotFoundException("No such user found!"));
    }
}
