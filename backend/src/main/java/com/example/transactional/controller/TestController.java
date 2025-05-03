package com.example.transactional.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;

@RestController
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/test")
    public String testConnection() {
        try {
            dataSource.getConnection();
            return "Conexión a la base de datos exitosa!";
        } catch (Exception e) {
            return "Error en la conexión: " + e.getMessage();
        }
    }
}
