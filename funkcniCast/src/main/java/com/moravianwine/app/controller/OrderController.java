package com.moravianwine.app.controller;

import com.moravianwine.app.model.Cart;
import com.moravianwine.app.model.Orders;
import com.moravianwine.app.model.Users;
import com.moravianwine.app.service.OrderDomain;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;

@Controller
@AllArgsConstructor
public class OrderController {
    private final OrderDomain orderDomain;

    @GetMapping("/create-order")
    public String createOrder(HttpSession session, Model model, Cart cart) throws SQLException {
        try{
            Users currentUser = (Users) session.getAttribute("currentUser");
            Cart userCart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                throw new RuntimeException("Košík nebyl nalezen.");
            }
            Orders newOrder = orderDomain.createOrder(userCart, currentUser);

            model.addAttribute("order", newOrder);
        }catch (SQLException e){
            session.setAttribute("errorMessage", e.getMessage());
        }
        return "create-order";
    }
}