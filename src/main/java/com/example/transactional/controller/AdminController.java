package com.example.transactional.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.transactional.model.Category;
import com.example.transactional.model.Provider;
import com.example.transactional.model.Role;
import com.example.transactional.repository.CategoryRepository;
import com.example.transactional.repository.ProviderRepository;
import com.example.transactional.repository.RoleRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProviderRepository providerRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @GetMapping
    public String adminDashboard(Model model, Authentication authentication) {
        // Verificar los diferentes roles
        boolean isAdmin = authentication.getAuthorities()
            .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        // Añadir atributos al modelo
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("canViewInventory", true);
        model.addAttribute("canViewReports", true);
        model.addAttribute("canViewUsers", true);
        
        return "admin/dashboard";
    }
    
    // =============== GESTIÓN DE CATEGORÍAS ===============
    
    @GetMapping("/categories")
    public String listCategories(Model model, Authentication authentication) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("newCategory", new Category());
        
        // Añadir atributos para la navegación
        model.addAttribute("isAdmin", true);
        model.addAttribute("canViewInventory", true);
        model.addAttribute("canViewReports", true);
        model.addAttribute("canViewUsers", true);
        
        return "admin/categories";
    }
    
    @PostMapping("/categories/create")
    public String createCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            // Validar que los campos requeridos estén presentes
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new RuntimeException("El nombre de la categoría es obligatorio");
            }
            
            // Generar un ID único para la categoría si no se proporciona
            if (category.getId() == null || category.getId().trim().isEmpty()) {
                // Generar un ID numérico aleatorio como String
                int randomId = (int) (Math.random() * 1000) + 1;
                category.setId(String.valueOf(randomId));
            }
            
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("success", "Categoría creada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la categoría: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
    
    @GetMapping("/categories/edit/{id}")
    public String showEditCategoryForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            
            model.addAttribute("category", category);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewUsers", true);
            
            return "admin/edit-category";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al obtener la categoría: " + e.getMessage());
            return "redirect:/admin/categories";
        }
    }
    
    @PostMapping("/categories/update")
    public String updateCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            // Validar que los campos requeridos estén presentes
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new RuntimeException("El nombre de la categoría es obligatorio");
            }
            
            // Asegurarse de que el ID no sea nulo
            if (category.getId() == null) {
                throw new RuntimeException("El ID de la categoría es obligatorio");
            }
            
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("success", "Categoría actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la categoría: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
    
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la categoría: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
    
    // =============== GESTIÓN DE PROVEEDORES ===============
    
    @GetMapping("/providers")
    public String listProviders(Model model, Authentication authentication) {
        model.addAttribute("providers", providerRepository.findAll());
        model.addAttribute("newProvider", new Provider());
        
        // Añadir atributos para la navegación
        model.addAttribute("isAdmin", true);
        model.addAttribute("canViewInventory", true);
        model.addAttribute("canViewReports", true);
        model.addAttribute("canViewUsers", true);
        
        return "admin/providers";
    }
    
    @PostMapping("/providers/create")
    public String createProvider(@ModelAttribute Provider provider, RedirectAttributes redirectAttributes) {
        try {
            // Validar que los campos requeridos estén presentes
            if (provider.getName() == null || provider.getName().trim().isEmpty()) {
                throw new RuntimeException("El nombre del proveedor es obligatorio");
            }
            
            // Generar un ID único para el proveedor si no se proporciona
            if (provider.getId() == null || provider.getId().trim().isEmpty()) {
                // Generar un ID numérico aleatorio como String
                int randomId = (int) (Math.random() * 1000) + 1;
                provider.setId(String.valueOf(randomId));
            }
            
            providerRepository.save(provider);
            redirectAttributes.addFlashAttribute("success", "Proveedor creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el proveedor: " + e.getMessage());
        }
        return "redirect:/admin/providers";
    }
    
    @GetMapping("/providers/edit/{id}")
    public String showEditProviderForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            
            model.addAttribute("provider", provider);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewUsers", true);
            
            return "admin/edit-provider";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al obtener el proveedor: " + e.getMessage());
            return "redirect:/admin/providers";
        }
    }
    
    @PostMapping("/providers/update")
    public String updateProvider(@ModelAttribute Provider provider, RedirectAttributes redirectAttributes) {
        try {
            // Validar que los campos requeridos estén presentes
            if (provider.getName() == null || provider.getName().trim().isEmpty()) {
                throw new RuntimeException("El nombre del proveedor es obligatorio");
            }
            
            // Asegurarse de que el ID no sea nulo
            if (provider.getId() == null) {
                throw new RuntimeException("El ID del proveedor es obligatorio");
            }
            
            providerRepository.save(provider);
            redirectAttributes.addFlashAttribute("success", "Proveedor actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el proveedor: " + e.getMessage());
        }
        return "redirect:/admin/providers";
    }
    
    @GetMapping("/providers/delete/{id}")
    public String deleteProvider(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            providerRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Proveedor eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el proveedor: " + e.getMessage());
        }
        return "redirect:/admin/providers";
    }
    
    // =============== GESTIÓN DE ROLES ===============
    
    @GetMapping("/roles")
    public String rolesRedirect() {
        return "redirect:/admin/manage-roles";
    }
    
    @GetMapping("/manage-roles")
    public String listRoles(Model model, Authentication authentication) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("newRole", new Role());
        
        // Añadir atributos para la navegación
        model.addAttribute("isAdmin", true);
        model.addAttribute("canViewInventory", true);
        model.addAttribute("canViewReports", true);
        model.addAttribute("canViewUsers", true);
        
        return "admin/roles";
    }
    
    @PostMapping("/manage-roles/create")
    public String createRole(@ModelAttribute Role role, RedirectAttributes redirectAttributes) {
        try {
            // Validar que los campos requeridos estén presentes
            if (role.getName() == null || role.getName().trim().isEmpty()) {
                throw new RuntimeException("El nombre del rol es obligatorio");
            }
            
            // El ID se genera automáticamente por ser auto_increment
            role.setId(null);
            
            roleRepository.save(role);
            redirectAttributes.addFlashAttribute("success", "Rol creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el rol: " + e.getMessage());
        }
        return "redirect:/admin/manage-roles";
    }
    
    @GetMapping("/manage-roles/edit/{id}")
    public String showEditRoleForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            
            model.addAttribute("role", role);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewUsers", true);
            
            return "admin/edit-role";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al obtener el rol: " + e.getMessage());
            return "redirect:/admin/manage-roles";
        }
    }
    
    @PostMapping("/manage-roles/update")
    public String updateRole(@ModelAttribute Role role, RedirectAttributes redirectAttributes) {
        try {
            // Validar que los campos requeridos estén presentes
            if (role.getName() == null || role.getName().trim().isEmpty()) {
                throw new RuntimeException("El nombre del rol es obligatorio");
            }
            
            // Validar que no se intente modificar los roles del sistema (IDs 1-4)
            if (role.getId() <= 4) {
                // Permitir la actualización pero solo del nombre, no eliminar
                Role existingRole = roleRepository.findById(role.getId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                existingRole.setName(role.getName());
                roleRepository.save(existingRole);
            } else {
                roleRepository.save(role);
            }
            
            redirectAttributes.addFlashAttribute("success", "Rol actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el rol: " + e.getMessage());
        }
        return "redirect:/admin/manage-roles";
    }
    
    @GetMapping("/manage-roles/delete/{id}")
    public String deleteRole(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Validar que no se intente eliminar los roles del sistema (IDs 1-4)
            if (id <= 4) {
                throw new RuntimeException("No se pueden eliminar los roles del sistema");
            }
            
            roleRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Rol eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el rol: " + e.getMessage());
        }
        return "redirect:/admin/manage-roles";
    }
}
