package com.example.demo.service;

import com.example.demo.exceptions.ElementAlreadyExistsException;
import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.exceptions.UnauthorizedAccessException;
import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Address;
import com.example.demo.model.entity.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public GetUserDTO register(HttpSession session, RegisterUserDTO registerUserDTO) {
        if (isLoggedIn(session)) {
            throw new UnauthorizedAccessException("You are already logged in!");
        }
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
        setSessionAttributes(session, user);
        return userEntityToDTO(user);
    }

    public GetUserDTO loginUser(HttpSession session, LoginUserDTO loginUserDTO) {
        if (isLoggedIn(session)) {
            throw new ElementNotFoundException("Already logged in!");
        }
        User user = userRepository.findByEmail(loginUserDTO.getEmail())
                .orElseThrow(() -> new ElementNotFoundException("Invalid email and/or password!"));
        if (passwordEncoder().matches(loginUserDTO.getPassword(), user.getPassword())) {
            setSessionAttributes(session, user);
            return userEntityToDTO(user);
        } else throw new ElementNotFoundException("Invalid email and/or password!");
    }

    public void logoutUser(HttpSession session) {
        if (!isLoggedIn(session)) {
            throw new UnauthorizedAccessException("You are not logged in!");
        }
        session.invalidate();
    }

    public GetAddressDTO addNewAddress(HttpSession session, PostAddressDTO postAddressDTO) {
        if (!isLoggedIn(session)) {
            throw new UnauthorizedAccessException("You must be logged in!");
        }
        User user = userRepository.findByEmail(session.getAttribute("email").toString())
                .orElseThrow(() -> new ElementNotFoundException("No such user!"));
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

    public List<GetAddressDTO> getAllAddresses(HttpSession session){
        if (!isLoggedIn(session)){
            throw new UnauthorizedAccessException("You must be logged in!");
        }
        List<GetAddressDTO> getAddressDTOList = new ArrayList<>();
        User user = userRepository.findByEmail(session.getAttribute("email").toString())
                .orElseThrow(() -> new ElementNotFoundException("No such user!"));
        user.getAddresses().stream()
                .map(address -> new GetAddressDTO(address.getId(), address.getCity(), address.getPhoneNumber(), address.getAddressDetails()))
                .forEach(getAddressDTO -> getAddressDTOList.add(getAddressDTO));
        return Collections.unmodifiableList(getAddressDTOList);
    }


    public void deleteAddress(HttpSession session, Long id) {
        if (!isLoggedIn(session)){
            throw new UnauthorizedAccessException("You must be logged in!");
        }
        if (!addressRepository.existsById(id)){
            throw new ElementNotFoundException("No such address found!");
        }
        addressRepository.deleteById(id);
    }


    public boolean isLoggedIn(HttpSession session) {
        if (session.getAttribute("email") == null) {
            return false;
        }
        return true;
    }

    public void checkIfAdmin(HttpSession session) {
        if (!isLoggedIn(session)) {
            throw new UnauthorizedAccessException("You are not logged in!");
        }
        if (session.getAttribute("isAdmin").equals(false)) {
            throw new UnauthorizedAccessException("You have no administration rights!");
        }
    }

    public GetUserDTO userEntityToDTO(User user) {
        return new GetUserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    private void setSessionAttributes(HttpSession session, User user) {
        session.setAttribute("email", user.getEmail());
        session.setAttribute("isAdmin", user.isAdmin());
    }

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
