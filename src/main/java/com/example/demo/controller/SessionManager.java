package com.example.demo.controller;

import com.example.demo.model.dto.AbstractUserDto;
import com.example.demo.model.dto.PostUserRegistrationFormDto;

import javax.servlet.http.HttpSession;

public class SessionManager {

    public static final String LOGGED_KEY = "logged";
    /* Session expires after 1 hour */
    private static final int EXPIRES_AFTER = 60 * 60;

    public static boolean validateLogged(HttpSession session) {
        return session.isNew() ? false : (session.getAttribute(LOGGED_KEY) == null) ? false : true;
    }

    public static void logUser(HttpSession session, AbstractUserDto userDto) {
        session.setMaxInactiveInterval(EXPIRES_AFTER);
        session.setAttribute(LOGGED_KEY, userDto);
    }
}
