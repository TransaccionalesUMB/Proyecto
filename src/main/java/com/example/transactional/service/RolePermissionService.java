package com.example.transactional.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar roles y permisos de usuarios
 * Proporciona métodos para verificar permisos y obtener información de roles
 */
@Service
public class RolePermissionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // Mapeo de roles a nombres amigables para mostrar en la interfaz
    private static final Map<String, String> ROLE_DISPLAY_NAMES = new HashMap<>();
    static {
        ROLE_DISPLAY_NAMES.put("ROLE_ADMIN", "Administrador");
        ROLE_DISPLAY_NAMES.put("ROLE_OPERADOR", "Operador");
        ROLE_DISPLAY_NAMES.put("ROLE_AUDITOR", "Auditor");
        ROLE_DISPLAY_NAMES.put("ROLE_CLIENTE", "Cliente");
    }
    
    /**
     * Obtiene el nombre amigable del rol para mostrar en la interfaz
     */
    public String getRoleDisplayName(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Invitado";
        }
        
        return authentication.getAuthorities().stream()
                .map(authority -> ROLE_DISPLAY_NAMES.getOrDefault(authority.getAuthority(), "Usuario"))
                .findFirst()
                .orElse("Usuario");
    }
    
    /**
     * Verifica si el usuario tiene un rol específico
     */
    public boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }
    
    /**
     * Verifica si el usuario tiene un permiso específico
     * Versión simplificada que asigna permisos basados en roles
     */
    public boolean hasPermission(Authentication authentication, String permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // Si es ADMIN, tiene todos los permisos
        if (hasRole(authentication, "ADMIN")) {
            return true;
        }
        
        try {
            // Asignación de permisos basada en roles
            // Esto evita consultas complejas a tablas que podrían no existir
            if ("GESTIONAR_INVENTARIO".equals(permission)) {
                return hasRole(authentication, "ADMIN") || hasRole(authentication, "OPERADOR");
            } else if ("GESTIONAR_PRODUCTOS".equals(permission)) {
                return hasRole(authentication, "ADMIN") || hasRole(authentication, "OPERADOR");
            } else if ("VER_REPORTES".equals(permission)) {
                return hasRole(authentication, "ADMIN") || hasRole(authentication, "OPERADOR") || hasRole(authentication, "AUDITOR");
            } else if ("GESTIONAR_USUARIOS".equals(permission)) {
                return hasRole(authentication, "ADMIN");
            } else if ("REALIZAR_PEDIDOS".equals(permission)) {
                return hasRole(authentication, "ADMIN") || hasRole(authentication, "OPERADOR") || hasRole(authentication, "CLIENTE");
            }
            
            // Para cualquier otro permiso no reconocido, denegar acceso
            return false;
        } catch (Exception e) {
            // En caso de error, registrar el error y devolver false
            System.err.println("Error al verificar permiso '" + permission + "': " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene todos los permisos de un usuario basados en su rol
     * Versión simplificada que asigna permisos basados en roles sin consultas complejas
     */
    public List<String> getUserPermissions(String username) {
        try {
            // Obtener el rol del usuario
            String sql = "SELECT r.nombre FROM usuario u JOIN rol r ON u.id_rol = r.id_rol WHERE u.email = ?";
            String roleName = null;
            
            try {
                roleName = jdbcTemplate.queryForObject(sql, String.class, username);
            } catch (Exception e) {
                System.err.println("Error al obtener rol para el usuario '" + username + "': " + e.getMessage());
                // Si no se puede obtener el rol, intentar con una consulta más simple
                sql = "SELECT id_rol FROM usuario WHERE email = ?";
                try {
                    Integer roleId = jdbcTemplate.queryForObject(sql, Integer.class, username);
                    roleName = getRoleNameById(roleId);
                } catch (Exception ex) {
                    System.err.println("Error al obtener id_rol para el usuario '" + username + "': " + ex.getMessage());
                }
            }
            
            // Lista de permisos basada en el rol
            List<String> permisos = new java.util.ArrayList<>();
            
            if ("ADMIN".equalsIgnoreCase(roleName)) {
                // Administrador tiene todos los permisos
                permisos.add("GESTIONAR_INVENTARIO");
                permisos.add("GESTIONAR_PRODUCTOS");
                permisos.add("VER_REPORTES");
                permisos.add("GESTIONAR_USUARIOS");
                permisos.add("REALIZAR_PEDIDOS");
            } else if ("OPERADOR".equalsIgnoreCase(roleName)) {
                // Operador tiene permisos limitados
                permisos.add("GESTIONAR_INVENTARIO");
                permisos.add("GESTIONAR_PRODUCTOS");
                permisos.add("VER_REPORTES");
                permisos.add("REALIZAR_PEDIDOS");
            } else if ("AUDITOR".equalsIgnoreCase(roleName)) {
                // Auditor solo puede ver reportes
                permisos.add("VER_REPORTES");
            } else if ("CLIENTE".equalsIgnoreCase(roleName)) {
                // Cliente solo puede realizar pedidos
                permisos.add("REALIZAR_PEDIDOS");
            }
            
            return permisos;
        } catch (Exception e) {
            // En caso de error, registrar el error y devolver una lista vacía
            System.err.println("Error al obtener permisos para el usuario '" + username + "': " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * Obtiene el ID del rol a partir de su nombre
     * Versión simplificada con mapeo estático para evitar consultas a la base de datos
     */
    public Integer getRoleIdByName(String roleName) {
        try {
            // Mapeo estático de nombres de roles a IDs
            if (roleName == null) {
                return null;
            }
            
            switch (roleName.toUpperCase()) {
                case "ADMIN":
                    return 1;
                case "OPERADOR":
                    return 2;
                case "CLIENTE":
                    return 3;
                case "AUDITOR":
                    return 4;
                default:
                    // Si no se reconoce el rol, intentar consultar la base de datos
                    try {
                        String sql = "SELECT id_rol FROM rol WHERE nombre = ?";
                        return jdbcTemplate.queryForObject(sql, Integer.class, roleName);
                    } catch (Exception ex) {
                        System.err.println("Error al consultar ID del rol '" + roleName + "' en la base de datos: " + ex.getMessage());
                        return null;
                    }
            }
        } catch (Exception e) {
            // En caso de error, registrar el error y devolver null
            System.err.println("Error al obtener ID del rol '" + roleName + "': " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene el nombre del rol a partir de su ID
     * Versión simplificada con mapeo estático para evitar consultas a la base de datos
     */
    public String getRoleNameById(Integer roleId) {
        try {
            // Mapeo estático de IDs a nombres de roles
            if (roleId == null) {
                return null;
            }
            
            switch (roleId) {
                case 1:
                    return "ADMIN";
                case 2:
                    return "OPERADOR";
                case 3:
                    return "CLIENTE";
                case 4:
                    return "AUDITOR";
                default:
                    // Si no se reconoce el ID, intentar consultar la base de datos
                    try {
                        String sql = "SELECT nombre FROM rol WHERE id_rol = ?";
                        return jdbcTemplate.queryForObject(sql, String.class, roleId);
                    } catch (Exception ex) {
                        System.err.println("Error al consultar nombre del rol con ID '" + roleId + "' en la base de datos: " + ex.getMessage());
                        return null;
                    }
            }
        } catch (Exception e) {
            // En caso de error, registrar el error y devolver null
            System.err.println("Error al obtener nombre del rol con ID '" + roleId + "': " + e.getMessage());
            return null;
        }
    }
}
