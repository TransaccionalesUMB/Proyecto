package com.example.transactional.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transactional.util.DatabaseConnectionTester;

import javax.sql.DataSource;

@RestController
@RequestMapping("/api/database")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseConnectionTester connectionTester;

    @GetMapping("/test")
    public ResponseEntity<String> testDirectConnection() {
        try {
            String result = connectionTester.testConnection();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error en la conexión: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getDatabaseStatus() {
        try {
            String status = connectionTester.getDatabaseStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error al obtener el estado: " + e.getMessage());
        }
    }
}
