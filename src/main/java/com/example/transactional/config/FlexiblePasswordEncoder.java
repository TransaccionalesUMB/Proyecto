package com.example.transactional.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Codificador de contraseñas flexible que puede manejar diferentes formatos de contraseñas.
 * Intenta primero con contraseñas sin encriptar, luego con BCrypt.
 */
@Component
public class FlexiblePasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        // Por defecto, no encriptamos las contraseñas nuevas
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Primero intentamos con contraseña sin encriptar (comparación directa)
        if (rawPassword.toString().equals(encodedPassword)) {
            System.out.println("Contraseña coincide usando comparación directa");
            return true;
        }

        // Si falla, intentamos con BCrypt
        try {
            if (encodedPassword.startsWith("$2a$") && bcryptEncoder.matches(rawPassword, encodedPassword)) {
                System.out.println("Contraseña coincide usando BCryptPasswordEncoder");
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error al verificar contraseña con BCrypt: " + e.getMessage());
        }

        System.out.println("La contraseña no coincide con ningún formato");
        return false;
    }
}
