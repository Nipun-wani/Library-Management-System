package com.example.bookinventory.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    // Load Login Page
    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    // Handle Login Form Submission
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        // Hardcoded admin credentials
        String adminUser = "admin";
        String adminPass = "admin123";

        if (username.equals(adminUser) && password.equals(adminPass)) {
            session.setAttribute("admin", true);
            return "redirect:/books";
        } else {
            model.addAttribute("error", true);
            return "login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
