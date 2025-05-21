package com.example.transactional.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * Utilidad para validar y corregir relaciones entre tablas en la base de datos
 * Esta clase se encarga de verificar que existan los registros necesarios para el funcionamiento
 * correcto de la aplicación y crea los registros mínimos necesarios si no existen.
 */
@Component
public class DatabaseRelationshipValidator {
    
    private static final Logger logger = Logger.getLogger(DatabaseRelationshipValidator.class.getName());
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Inicializa la validación de relaciones al arrancar la aplicación
     */
    @PostConstruct
    public void init() {
        try {
            logger.info("Iniciando validación de relaciones en la base de datos...");
            
            // Verificar y crear categorías mínimas
            ensureCategoriesExist();
            
            // Verificar y crear proveedores mínimos
            ensureProvidersExist();
            
            // Verificar y crear bodegas mínimas
            ensureWarehousesExist();
            
            // Verificar y crear ubicaciones mínimas
            ensureLocationsExist();
            
            logger.info("Validación de relaciones completada con éxito.");
        } catch (Exception e) {
            logger.severe("Error al validar relaciones en la base de datos: " + e.getMessage());
        }
    }
    
    /**
     * Verifica que existan categorías mínimas y las crea si no existen
     */
    private void ensureCategoriesExist() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Categoria", Integer.class);
        
        if (count == null || count == 0) {
            logger.info("No se encontraron categorías. Creando categoría por defecto...");
            jdbcTemplate.update(
                "INSERT INTO Categoria (id_categoria, nombre, descripcion) VALUES (?, ?, ?)",
                1, "Categoría General", "Categoría por defecto para productos sin clasificar"
            );
        }
    }
    
    /**
     * Verifica que existan proveedores mínimos y los crea si no existen
     */
    private void ensureProvidersExist() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Proveedor", Integer.class);
        
        if (count == null || count == 0) {
            logger.info("No se encontraron proveedores. Creando proveedor por defecto...");
            jdbcTemplate.update(
                "INSERT INTO Proveedor (id_proveedor, nombre, contacto) VALUES (?, ?, ?)",
                1, "Proveedor General", "Proveedor por defecto"
            );
        }
    }
    
    /**
     * Verifica que existan bodegas mínimas y las crea si no existen
     */
    private void ensureWarehousesExist() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Bodega", Integer.class);
        
        if (count == null || count == 0) {
            logger.info("No se encontraron bodegas. Creando bodega por defecto...");
            jdbcTemplate.update(
                "INSERT INTO Bodega (id_bodega, nombre, ubicacion) VALUES (?, ?, ?)",
                1, "Bodega Principal", "Ubicación Principal"
            );
        }
    }
    
    /**
     * Verifica que existan ubicaciones mínimas y las crea si no existen
     */
    private void ensureLocationsExist() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM UbicacionBodega", Integer.class);
        
        if (count == null || count == 0) {
            logger.info("No se encontraron ubicaciones. Creando ubicación por defecto...");
            jdbcTemplate.update(
                "INSERT INTO UbicacionBodega (id_ubicacion, id_bodega, pasillo, estante, nivel) VALUES (?, ?, ?, ?, ?)",
                1, 1, "A", "01", "1"
            );
        }
    }
    
    /**
     * Verifica si existe un producto con el ID especificado
     * @param productId ID del producto
     * @return true si existe, false en caso contrario
     */
    public boolean productExists(Integer productId) {
        if (productId == null) {
            return false;
        }
        
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Producto WHERE id_producto = ?", 
            Integer.class, 
            productId
        );
        
        return count != null && count > 0;
    }
    
    /**
     * Verifica si existe una categoría con el ID especificado
     * @param categoryId ID de la categoría
     * @return true si existe, false en caso contrario
     */
    public boolean categoryExists(Integer categoryId) {
        if (categoryId == null) {
            return false;
        }
        
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Categoria WHERE id_categoria = ?", 
            Integer.class, 
            categoryId
        );
        
        return count != null && count > 0;
    }
    
    /**
     * Verifica si existe un proveedor con el ID especificado
     * @param providerId ID del proveedor
     * @return true si existe, false en caso contrario
     */
    public boolean providerExists(Integer providerId) {
        if (providerId == null) {
            return false;
        }
        
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Proveedor WHERE id_proveedor = ?", 
            Integer.class, 
            providerId
        );
        
        return count != null && count > 0;
    }
    
    /**
     * Verifica si existe una bodega con el ID especificado
     * @param warehouseId ID de la bodega
     * @return true si existe, false en caso contrario
     */
    public boolean warehouseExists(Integer warehouseId) {
        if (warehouseId == null) {
            return false;
        }
        
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Bodega WHERE id_bodega = ?", 
            Integer.class, 
            warehouseId
        );
        
        return count != null && count > 0;
    }
    
    /**
     * Verifica si existe una ubicación con el ID especificado
     * @param locationId ID de la ubicación
     * @return true si existe, false en caso contrario
     */
    public boolean locationExists(Integer locationId) {
        if (locationId == null) {
            return false;
        }
        
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM UbicacionBodega WHERE id_ubicacion = ?", 
            Integer.class, 
            locationId
        );
        
        return count != null && count > 0;
    }
    
    /**
     * Verifica si existe un lote con el ID especificado
     * @param batchId ID del lote
     * @return true si existe, false en caso contrario
     */
    public boolean batchExists(Integer batchId) {
        if (batchId == null) {
            return false;
        }
        
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Lote WHERE id_lote = ?", 
            Integer.class, 
            batchId
        );
        
        return count != null && count > 0;
    }
}
