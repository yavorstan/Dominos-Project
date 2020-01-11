package com.example.demo.controller;

import com.example.demo.model.dto.AbstractUserDto;
import com.example.demo.model.dto.PostLoginUserDto;
import com.example.demo.model.dto.PostUserRegistrationFormDto;
import com.example.demo.model.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void register(HttpSession session, @RequestBody PostUserRegistrationFormDto userDto){
        userService.addUser(userDto);
        SessionManager.logUser(session, userDto);
    }

    @PostMapping("/login")
    public void login(HttpSession session, @RequestBody PostLoginUserDto userDto){
        userService.logUser(userDto);
        SessionManager.logUser(session,userDto);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }

}
