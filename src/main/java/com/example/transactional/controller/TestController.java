package com.example.transactional.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/test")
    public ResponseEntity<String> testConnection() {
        try (Connection conn = dataSource.getConnection()) {
            return ResponseEntity.ok("Conexión a la base de datos exitosa!");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Error en la conexión: " + e.getMessage());
        }
    }
}
