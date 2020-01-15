package com.example.demo.controller;

import com.example.demo.exceptions.ErrorCreatingEntityException;
import com.example.demo.exceptions.UnauthorizedAccessException;
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

    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/register")
    public ResponseEntity<GetUserDTO> registerUser(HttpSession session,
                                                   @Valid @RequestBody RegisterUserDTO registerUserDTO,
                                                   Errors errors) {
        if (sessionManager.isLoggedIn(session)) {
            throw new UnauthorizedAccessException("You are already logged in!");
        }
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetUserDTO getUserDTO = userService.register(registerUserDTO);
        sessionManager.setSessionAttributes(session, getUserDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(getUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<GetUserDTO> loginUser(HttpSession session, @RequestBody LoginUserDTO loginUserDTO) {
        if (sessionManager.isLoggedIn(session)) {
            throw new UnauthorizedAccessException("You are already logged in!");
        }
        GetUserDTO getUserDTO = userService.loginUser(loginUserDTO);
        sessionManager.setSessionAttributes(session, getUserDTO.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(getUserDTO);
    }

    @PostMapping("/logout")
    public void logoutUser(HttpSession session) {
        sessionManager.checkIfLoggedIn(session);
        session.invalidate();
    }

    @PostMapping("/addresses")
    public ResponseEntity<GetAddressDTO> addNewAddress(HttpSession session,
                                                       @Valid @RequestBody PostAddressDTO postAddressDTO,
                                                       Errors errors) {
        sessionManager.checkIfLoggedIn(session);
        if (errors.hasErrors()) {
            throw new ErrorCreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        GetAddressDTO getAddressDTO = userService.addNewAddress(sessionManager.findUserByEmail(session), postAddressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(getAddressDTO);
    }

    @GetMapping("/addresses")
    public List<GetAddressDTO> getAllAddresses(HttpSession session) {
        sessionManager.checkIfLoggedIn(session);
        return userService.getAllAddresses(sessionManager.findUserByEmail(session));
    }

    @DeleteMapping("/addresses/{id}")
    public void deleteAddress(HttpSession session, @PathVariable(value = "id") Long id) {
        sessionManager.checkIfLoggedIn(session);
        userService.deleteAddress(id);
    }

}
