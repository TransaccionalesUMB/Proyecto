package com.example.transactional.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model,
                       Authentication authentication) {
        
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña inválidos");
        }

        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión correctamente");
        }

        return "login";
    }
    
    // Este método se ha eliminado para evitar conflictos con HomeController
    // @GetMapping("/home")
    // public String home(Authentication authentication) {
    //     if (authentication == null || !authentication.isAuthenticated()) {
    //         return "redirect:/login";
    //     }
    //     return "home";
    // }
}
