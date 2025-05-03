package com.example.transactional.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseConnectionTester {

    @Autowired
    private DataSource dataSource;

    public String testConnection() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            List<String> tables = new ArrayList<>();
            ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
            
            return String.format("Conexión exitosa! Tablas encontradas: %s", String.join(", ", tables));
        }
    }

    public String getDatabaseStatus() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            return String.format(
                "Estado de la base de datos:\n" +
                "- Producto: %s\n" +
                "- Versión: %s\n" +
                "- Driver: %s %s\n" +
                "- URL: %s\n" +
                "- Usuario: %s",
                metaData.getDatabaseProductName(),
                metaData.getDatabaseProductVersion(),
                metaData.getDriverName(),
                metaData.getDriverVersion(),
                metaData.getURL(),
                metaData.getUserName()
            );
        }
    }
}
