package com.example.transactional.controller;

import com.example.transactional.model.Usuario;
import com.example.transactional.repository.UsuarioRepository;
import com.example.transactional.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String root(Authentication authentication) {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getName().equals("anonymousUser")) {
            System.out.println("Usuario no autenticado, redirigiendo a login");
            return "redirect:/login";
        }
        
        try {
            System.out.println("Usuario autenticado en /home: " + authentication.getName());
            System.out.println("Autoridades: " + authentication.getAuthorities());
            
            // Verificar los diferentes roles usando el servicio
            boolean isAdmin = false;
            boolean isOperador = false;
            boolean isAuditor = false;
            boolean isCliente = false;
            
            try {
                isAdmin = rolePermissionService.hasRole(authentication, "ADMIN");
                System.out.println("¿Es admin? " + isAdmin);
            } catch (Exception e) {
                System.err.println("Error al verificar rol ADMIN: " + e.getMessage());
            }
            
            try {
                isOperador = rolePermissionService.hasRole(authentication, "OPERADOR");
                System.out.println("¿Es operador? " + isOperador);
            } catch (Exception e) {
                System.err.println("Error al verificar rol OPERADOR: " + e.getMessage());
            }
            
            try {
                isAuditor = rolePermissionService.hasRole(authentication, "AUDITOR");
                System.out.println("¿Es auditor? " + isAuditor);
            } catch (Exception e) {
                System.err.println("Error al verificar rol AUDITOR: " + e.getMessage());
            }
            
            try {
                isCliente = rolePermissionService.hasRole(authentication, "CLIENTE");
                System.out.println("¿Es cliente? " + isCliente);
            } catch (Exception e) {
                System.err.println("Error al verificar rol CLIENTE: " + e.getMessage());
            }
            
            // Si el usuario no tiene ningún rol asignado, asignarle el rol de cliente por defecto
            if (!isAdmin && !isOperador && !isAuditor && !isCliente) {
                System.out.println("Usuario sin rol asignado, asignando rol CLIENTE por defecto");
                isCliente = true;
            }
            
            // Establecer valores por defecto para todos los atributos del modelo para evitar errores
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("isOperador", isOperador);
            model.addAttribute("isAuditor", isAuditor);
            model.addAttribute("isCliente", isCliente);
            model.addAttribute("canViewInventory", false);
            model.addAttribute("canViewProducts", false);
            model.addAttribute("canViewReports", false);
            model.addAttribute("canManageUsers", false);
            model.addAttribute("canMakeOrders", false);
            
            // Los atributos básicos de roles ya están establecidos arriba
            
            // Añadir nombre de usuario al modelo
            model.addAttribute("username", authentication.getName());
            
            // Añadir nombre de rol para mostrar
            try {
                model.addAttribute("roleDisplayName", rolePermissionService.getRoleDisplayName(authentication));
            } catch (Exception e) {
                System.err.println("Error al obtener nombre de rol: " + e.getMessage());
                model.addAttribute("roleDisplayName", "Usuario");
            }
            
            // Permisos específicos para cada sección - con valores por defecto en caso de error
            try {
                model.addAttribute("canViewInventory", 
                    rolePermissionService.hasPermission(authentication, "GESTIONAR_INVENTARIO"));
            } catch (Exception e) {
                System.err.println("Error al verificar permiso GESTIONAR_INVENTARIO: " + e.getMessage());
                model.addAttribute("canViewInventory", isAdmin || isOperador);
            }
            
            try {
                model.addAttribute("canViewProducts", 
                    rolePermissionService.hasPermission(authentication, "GESTIONAR_PRODUCTOS"));
            } catch (Exception e) {
                System.err.println("Error al verificar permiso GESTIONAR_PRODUCTOS: " + e.getMessage());
                model.addAttribute("canViewProducts", isAdmin || isOperador);
            }
            
            try {
                model.addAttribute("canViewReports", 
                    rolePermissionService.hasPermission(authentication, "VER_REPORTES"));
            } catch (Exception e) {
                System.err.println("Error al verificar permiso VER_REPORTES: " + e.getMessage());
                model.addAttribute("canViewReports", isAdmin || isOperador || isAuditor);
            }
            
            try {
                model.addAttribute("canManageUsers", 
                    rolePermissionService.hasPermission(authentication, "GESTIONAR_USUARIOS"));
            } catch (Exception e) {
                System.err.println("Error al verificar permiso GESTIONAR_USUARIOS: " + e.getMessage());
                model.addAttribute("canManageUsers", isAdmin);
            }
            
            try {
                model.addAttribute("canMakeOrders", 
                    rolePermissionService.hasPermission(authentication, "REALIZAR_PEDIDOS"));
            } catch (Exception e) {
                System.err.println("Error al verificar permiso REALIZAR_PEDIDOS: " + e.getMessage());
                model.addAttribute("canMakeOrders", isAdmin || isOperador || isCliente);
            }
            
            // Obtener información del usuario para mostrar en la página
            try {
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(authentication.getName());
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    model.addAttribute("userName", usuario.getNombre());
                    model.addAttribute("userEmail", usuario.getEmail());
                    model.addAttribute("userRole", rolePermissionService.getRoleDisplayName(authentication));
                } else {
                    // Si no se encuentra el usuario, establecer valores predeterminados
                    model.addAttribute("userName", "Usuario");
                    model.addAttribute("userEmail", authentication.getName());
                    model.addAttribute("userRole", "Usuario");
                }
            } catch (Exception e) {
                System.err.println("Error al obtener información del usuario: " + e.getMessage());
                // Establecer valores predeterminados
                model.addAttribute("userName", "Usuario");
                model.addAttribute("userEmail", authentication.getName());
                model.addAttribute("userRole", "Usuario");
            }
            
            return "home";
        } catch (Exception e) {
            // En caso de error, registrar el error y redirigir a la página de login
            System.err.println("Error al cargar la página de inicio: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/login?error=true";
        }
    }
}
