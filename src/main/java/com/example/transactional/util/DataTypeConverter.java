package com.example.transactional.util;

import org.springframework.stereotype.Component;

/**
 * Utilidad para convertir entre diferentes tipos de datos
 * Esta clase proporciona métodos para convertir de manera segura entre String, Integer y otros tipos
 * evitando errores de conversión y proporcionando valores por defecto cuando sea necesario.
 */
@Component
public class DataTypeConverter {
    
    /**
     * Convierte un String a Integer de manera segura
     * @param value Valor a convertir
     * @param defaultValue Valor por defecto si la conversión falla
     * @return Integer convertido o valor por defecto
     */
    public static Integer toInteger(String value, Integer defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convierte un String a Integer de manera segura
     * @param value Valor a convertir
     * @return Integer convertido o null
     */
    public static Integer toInteger(String value) {
        return toInteger(value, null);
    }
    
    /**
     * Convierte un Integer a String de manera segura
     * @param value Valor a convertir
     * @param defaultValue Valor por defecto si el valor es null
     * @return String convertido o valor por defecto
     */
    public static String toString(Integer value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        
        return value.toString();
    }
    
    /**
     * Convierte un Integer a String de manera segura
     * @param value Valor a convertir
     * @return String convertido o null
     */
    public static String toString(Integer value) {
        return toString(value, null);
    }
    
    /**
     * Verifica si un String puede ser convertido a Integer
     * @param value Valor a verificar
     * @return true si puede ser convertido, false en caso contrario
     */
    public static boolean isInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Obtiene un valor por defecto para un ID de categoría
     * @return ID de categoría por defecto ("1")
     */
    public static String getDefaultCategoryId() {
        return "1"; // Categoría General
    }
    
    /**
     * Obtiene un valor por defecto para un ID de proveedor
     * @return ID de proveedor por defecto ("1")
     */
    public static String getDefaultProviderId() {
        return "1"; // Proveedor General
    }
    
    /**
     * Obtiene un valor por defecto para un ID de bodega
     * @return ID de bodega por defecto (1)
     */
    public static Integer getDefaultWarehouseId() {
        return 1; // Bodega Principal
    }
    
    /**
     * Obtiene un valor por defecto para un ID de ubicación
     * @return ID de ubicación por defecto (1)
     */
    public static Integer getDefaultLocationId() {
        return 1; // Ubicación Principal
    }
}
