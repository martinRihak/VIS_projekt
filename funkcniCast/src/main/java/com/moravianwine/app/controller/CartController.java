package com.moravianwine.app.controller;

import com.moravianwine.app.model.Cart;
import com.moravianwine.app.model.Users;
import com.moravianwine.app.repository.CartMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;

@Controller
@AllArgsConstructor
public class CartController {
    private final CartMapper cartMapper;


    @GetMapping("/user-cart")
    public String getCart(HttpSession session, Model model) throws SQLException {
        Users currentUser = (Users) session.getAttribute("currentUser");
        Cart cart = (Cart) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        return "user-cart";
    }
}
