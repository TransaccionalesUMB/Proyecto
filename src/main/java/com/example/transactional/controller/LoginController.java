package com.example.transactional.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        
        // Verificar si el usuario ya está autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            System.out.println("Usuario ya autenticado: " + authentication.getName() + ", redirigiendo a /home");
            return "redirect:/home";
        }

        // Manejar mensajes de error
        if (error != null) {
            System.out.println("Error de login detectado");
            model.addAttribute("error", "Usuario o contraseña inválidos. Por favor, inténtelo de nuevo.");
        }

        // Manejar mensajes de cierre de sesión
        if (logout != null) {
            System.out.println("Logout detectado");
            model.addAttribute("message", "Has cerrado sesión correctamente");
        }

        System.out.println("Mostrando página de login");
        return "login";
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
