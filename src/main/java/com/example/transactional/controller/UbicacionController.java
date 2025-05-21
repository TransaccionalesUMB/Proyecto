package com.example.transactional.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.transactional.model.Bodega;
import com.example.transactional.model.UbicacionBodega;
import com.example.transactional.service.BodegaService;
import com.example.transactional.service.UbicacionBodegaService;
import com.example.transactional.service.StockService;

import java.util.Optional;

/**
 * Controlador para la gestión de ubicaciones dentro de las bodegas
 */
@Controller
@RequestMapping("/admin/ubicaciones")
@PreAuthorize("hasRole('ADMIN')")
public class UbicacionController {
    
    @Autowired
    private UbicacionBodegaService ubicacionService;
    
    @Autowired
    private BodegaService bodegaService;
    
    @Autowired
    private StockService stockService;
    
    /**
     * Guardar una ubicación (nueva o actualización)
     */
    @PostMapping("/save")
    public String guardarUbicacion(@ModelAttribute UbicacionBodega ubicacion, 
                                  @RequestParam("bodegaId") Integer bodegaId,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Obtener la bodega
            Optional<Bodega> bodegaOpt = bodegaService.getBodegaById(bodegaId);
            if (!bodegaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "La bodega no existe");
                return "redirect:/admin/almacenes";
            }
            
            Bodega bodega = bodegaOpt.get();
            ubicacion.setBodega(bodega);
            
            if (ubicacion.getId() != null) {
                // Actualización
                ubicacionService.updateUbicacion(ubicacion);
                redirectAttributes.addFlashAttribute("mensaje", "Ubicación actualizada correctamente");
            } else {
                // Nueva
                ubicacionService.saveUbicacion(ubicacion);
                redirectAttributes.addFlashAttribute("mensaje", "Ubicación creada correctamente");
            }
            return "redirect:/admin/bodegas/" + bodegaId + "/ubicaciones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la ubicación: " + e.getMessage());
            return "redirect:/admin/bodegas/" + bodegaId + "/ubicaciones";
        }
    }
    
    /**
     * Eliminar una ubicación
     */
    @GetMapping("/delete/{id}")
    public String eliminarUbicacion(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si la ubicación existe
            Optional<UbicacionBodega> ubicacionOpt = ubicacionService.getUbicacionById(id);
            if (!ubicacionOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "La ubicación no existe");
                return "redirect:/admin/almacenes";
            }
            
            UbicacionBodega ubicacion = ubicacionOpt.get();
            Integer bodegaId = ubicacion.getBodega().getId();
            
            // Verificar si tiene stock asociado
            boolean tieneStock = stockService.existsStockByUbicacionId(id);
            if (tieneStock) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar la ubicación porque tiene stock asociado. Traslade primero el stock a otra ubicación.");
                return "redirect:/admin/bodegas/" + bodegaId + "/ubicaciones";
            }
            
            // Eliminar la ubicación
            ubicacionService.deleteUbicacion(id);
            redirectAttributes.addFlashAttribute("mensaje", "Ubicación eliminada correctamente");
            return "redirect:/admin/bodegas/" + bodegaId + "/ubicaciones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la ubicación: " + e.getMessage());
            return "redirect:/admin/almacenes";
        }
    }
}
