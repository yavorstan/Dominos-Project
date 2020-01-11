package com.example.demo.service;

import com.example.demo.controller.SessionManager;
import com.example.demo.exceptions.CredentialsException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.model.dto.PostLoginUserDto;
import com.example.demo.model.dto.PostUserRegistrationFormDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public static final String PASSWORDS_MISMATCH_MESSAGE = "The passwords do not match. Please try again!";
    public static final String EMAILS_MISMATCH_MESSAGE = "The emails do not match. Please try again!";
    @Autowired
    private UserRepository userRepository;

    public long addUser(PostUserRegistrationFormDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmationPassword())) {
            throw new CredentialsException(PASSWORDS_MISMATCH_MESSAGE);
        }
        if (!userDto.getEmail().equals(userDto.getConfirmationEmail())) {
            throw new CredentialsException(EMAILS_MISMATCH_MESSAGE);
        }
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new CredentialsException("User with that email already exist. " +
                    "If you have access to the following email" +
                    ", you can request a new password, " +
                    "otherwise please enter other email address!");
        }
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());//TODO encrypt
        return userRepository.save(user).getId();
    }

    public void logUser(PostLoginUserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            throw new UnauthorizedException("User with such email does not exist. " +
                    "Please check your credentials!");
        }
        if(!user.getPassword().equals(userDto.getPassword())){
            throw new UnauthorizedException("Invalid combination of email and password. " +
                    "Please check your credentials!");
        }
        userRepository.save(user);
    }
}
