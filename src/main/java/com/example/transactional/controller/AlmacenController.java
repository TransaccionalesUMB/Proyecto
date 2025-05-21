package com.example.transactional.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.transactional.model.Almacen;
import com.example.transactional.model.Bodega;
import com.example.transactional.service.AlmacenService;
import com.example.transactional.service.BodegaService;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para la gestión de almacenes
 */
@Controller
@RequestMapping("/admin/almacenes")
@PreAuthorize("hasRole('ADMIN')")
public class AlmacenController {
    
    @Autowired
    private AlmacenService almacenService;
    
    @Autowired
    private BodegaService bodegaService;
    
    /**
     * Listar todos los almacenes
     */
    @GetMapping
    public String listarAlmacenes(Model model) {
        try {
            List<Almacen> almacenes = almacenService.getAllAlmacenes();
            model.addAttribute("almacenes", almacenes);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "admin/almacenes";
        } catch (org.hibernate.exception.SQLGrammarException e) {
            // Manejar específicamente errores de SQL
            model.addAttribute("error", "Hubo un problema al cargar los almacenes: could not extract ResultSet");
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "admin/almacenes";
        } catch (Exception e) {
            // Manejar otros errores
            model.addAttribute("error", "Hubo un problema al cargar los almacenes: " + e.getMessage());
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "admin/almacenes";
        }
    }
    
    /**
     * Guardar un almacén (nuevo o actualización)
     */
    @PostMapping("/save")
    public String guardarAlmacen(@ModelAttribute Almacen almacen, RedirectAttributes redirectAttributes) {
        try {
            if (almacen.getId() != null) {
                // Actualización
                almacenService.updateAlmacen(almacen);
                redirectAttributes.addFlashAttribute("mensaje", "Almacén actualizado correctamente");
            } else {
                // Nuevo
                almacenService.saveAlmacen(almacen);
                redirectAttributes.addFlashAttribute("mensaje", "Almacén creado correctamente");
            }
            return "redirect:/admin/almacenes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el almacén: " + e.getMessage());
            return "redirect:/admin/almacenes";
        }
    }
    
    /**
     * Eliminar un almacén
     */
    @GetMapping("/delete/{id}")
    public String eliminarAlmacen(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si el almacén existe
            Optional<Almacen> almacenOpt = almacenService.getAlmacenById(id);
            if (!almacenOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El almacén no existe");
                return "redirect:/admin/almacenes";
            }
            
            // Verificar si tiene bodegas asociadas
            List<Bodega> bodegas = bodegaService.getBodegasByAlmacen(id);
            if (!bodegas.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar el almacén porque tiene bodegas asociadas. Elimine primero las bodegas.");
                return "redirect:/admin/almacenes";
            }
            
            // Eliminar el almacén
            almacenService.deleteAlmacen(id);
            redirectAttributes.addFlashAttribute("mensaje", "Almacén eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el almacén: " + e.getMessage());
        }
        return "redirect:/admin/almacenes";
    }
    
    /**
     * Ver las bodegas de un almacén
     */
    @GetMapping("/{id}/bodegas")
    public String verBodegas(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        // Verificar si el almacén existe
        Optional<Almacen> almacenOpt = almacenService.getAlmacenById(id);
        if (!almacenOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El almacén no existe");
            return "redirect:/admin/almacenes";
        }
        
        Almacen almacen = almacenOpt.get();
        List<Bodega> bodegas = bodegaService.getBodegasByAlmacen(id);
        
        model.addAttribute("almacen", almacen);
        model.addAttribute("bodegas", bodegas);
        
        return "admin/bodegas";
    }
}
