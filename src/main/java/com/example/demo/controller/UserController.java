package com.example.demo.controller;

import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.model.dto.*;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<GetUserDTO> registerUser(HttpSession session,
                                                   @Valid @RequestBody RegisterUserDTO registerUserDTO,
                                                   Errors errors) {
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetUserDTO getUserDTO = userService.register(session, registerUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(getUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<GetUserDTO> loginUser(HttpSession session, @RequestBody LoginUserDTO loginUserDTO) {
        GetUserDTO getUserDTO = userService.loginUser(session, loginUserDTO);
        return ResponseEntity.status(HttpStatus.OK).body(getUserDTO);
    }

    @PostMapping("/logout")
    public void logoutUser(HttpSession session) {
        userService.logoutUser(session);
    }

    @PostMapping("/addresses/addAddress")
    public ResponseEntity<GetAddressDTO> addNewAddress(HttpSession session,
                                                       @Valid @RequestBody PostAddressDTO postAddressDTO,
                                                       Errors errors){
        if (errors.hasErrors()){
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetAddressDTO getAddressDTO = userService.addNewAddress(session, postAddressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(getAddressDTO);
    }

    @GetMapping("/addresses")
    public List<GetAddressDTO> getAllAddresses(HttpSession session){
        return userService.getAllAddresses(session);
    }

    @DeleteMapping("/addresses/{id}")
    public void deleteAddress(HttpSession session, @PathVariable (value = "id") Long id){
        userService.deleteAddress(session, id);
    }

}
