package com.moravianwine.app.controller;

import com.moravianwine.app.model.Users;
import com.moravianwine.app.repository.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;

@Controller
@AllArgsConstructor
public class RegistrationController {
    private final UserMapper um;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("Register jsem tu");
        Users.RegUser user = new Users.RegUser();
        Users.Address address = new Users.Address();
        model.addAttribute("user", user);
        model.addAttribute("address", address);
        return "registration";
    }
    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute("user") Users.RegUser user , @ModelAttribute("address")Users.Address ad, Model model) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRole('1');
        user.setAddress(ad);
        try{
            um.insertUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/login";
    }
}
