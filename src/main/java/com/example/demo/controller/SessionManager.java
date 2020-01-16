package com.example.demo.controller;

import com.example.demo.exceptions.ElementNotFoundException;
import com.example.demo.exceptions.UnauthorizedAccessException;
import com.example.demo.model.entity.PizzaOrder;
import com.example.demo.model.entity.User;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Component
public class SessionManager {

    private UserService userService;

    public SessionManager(UserService userService) {
        this.userService = userService;
    }

    public void setSessionAttributes(HttpSession session, String email) {
        session.setAttribute("email", email);
        session.setAttribute("cart", new ArrayList<PizzaOrder>());
        if (session.getAttribute("email").equals("admin@site.com")){
            session.setAttribute("isAdmin", true);
        }
        else session.setAttribute("isAdmin", false);
    }

    public void setCartAttribute(HttpSession session, ArrayList<PizzaOrder> cart) {
        session.setAttribute("cart", cart);
    }

    public ArrayList<PizzaOrder> getCartAttribute(HttpSession session) {
        return (ArrayList<PizzaOrder>) session.getAttribute("cart");
    }

    public void emptyCart(HttpSession session) {
        session.setAttribute("cart", new ArrayList<PizzaOrder>());
    }

    public User findUserByEmail(HttpSession session) {
        return userService.findByEmail(session.getAttribute("email").toString());
    }

    public void checkIfCartIsEmpty(HttpSession session) {
        checkIfLoggedIn(session);
        ArrayList<PizzaOrder> cart = (ArrayList<PizzaOrder>) session.getAttribute("cart");
        if (cart.isEmpty()) {
            throw new ElementNotFoundException("Your cart is empty!");
        }
    }

    public void checkIfLoggedIn(HttpSession session) {
        if (session.getAttribute("email") == null) {
            throw new UnauthorizedAccessException("You are not logged in!");
        }
    }

    public void checkIfAdmin(HttpSession session) {
        checkIfLoggedIn(session);
        if (session.getAttribute("isAdmin").equals(false)) {
            throw new UnauthorizedAccessException("You have no administration rights!");
        }
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("email") != null;
    }

}
