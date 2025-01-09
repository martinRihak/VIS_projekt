package com.moravianwine.app.controller;

import com.moravianwine.app.model.Users;
import com.moravianwine.app.repository.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;


public class UserController {
    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @PostMapping("/select-user")
    public String selectUser(HttpSession session) throws SQLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Users selectedUser = userMapper.findByUsername(userDetails.getUsername());
        session.setAttribute("currentUser", selectedUser);
        return "redirect:/wines";
    }
}
