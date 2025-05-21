package com.example.transactional.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.transactional.model.Almacen;
import com.example.transactional.model.Bodega;
import com.example.transactional.model.UbicacionBodega;
import com.example.transactional.service.AlmacenService;
import com.example.transactional.service.BodegaService;
import com.example.transactional.service.UbicacionBodegaService;
import com.example.transactional.service.StockService;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para la gestión de bodegas
 */
@Controller
@RequestMapping("/admin/bodegas")
@PreAuthorize("hasRole('ADMIN')")
public class BodegaController {
    
    @Autowired
    private BodegaService bodegaService;
    
    @Autowired
    private AlmacenService almacenService;
    
    @Autowired
    private UbicacionBodegaService ubicacionService;
    
    @Autowired
    private StockService stockService;
    
    /**
     * Guardar una bodega (nueva o actualización)
     */
    @PostMapping("/save")
    public String guardarBodega(@ModelAttribute Bodega bodega, 
                               @RequestParam("almacenId") Integer almacenId,
                               RedirectAttributes redirectAttributes) {
        try {
            // Obtener el almacén
            Optional<Almacen> almacenOpt = almacenService.getAlmacenById(almacenId);
            if (!almacenOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El almacén no existe");
                return "redirect:/admin/almacenes";
            }
            
            Almacen almacen = almacenOpt.get();
            bodega.setAlmacen(almacen);
            
            if (bodega.getId() != null) {
                // Actualización
                bodegaService.updateBodega(bodega);
                redirectAttributes.addFlashAttribute("mensaje", "Bodega actualizada correctamente");
            } else {
                // Nueva
                bodegaService.saveBodega(bodega);
                redirectAttributes.addFlashAttribute("mensaje", "Bodega creada correctamente");
            }
            return "redirect:/admin/almacenes/" + almacenId + "/bodegas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la bodega: " + e.getMessage());
            return "redirect:/admin/almacenes/" + almacenId + "/bodegas";
        }
    }
    
    /**
     * Eliminar una bodega
     */
    @GetMapping("/delete/{id}")
    public String eliminarBodega(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si la bodega existe
            Optional<Bodega> bodegaOpt = bodegaService.getBodegaById(id);
            if (!bodegaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "La bodega no existe");
                return "redirect:/admin/almacenes";
            }
            
            Bodega bodega = bodegaOpt.get();
            Integer almacenId = bodega.getAlmacen().getId();
            
            // Verificar si tiene ubicaciones asociadas
            List<UbicacionBodega> ubicaciones = ubicacionService.getUbicacionesByBodega(id);
            if (!ubicaciones.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar la bodega porque tiene ubicaciones asociadas. Elimine primero las ubicaciones.");
                return "redirect:/admin/almacenes/" + almacenId + "/bodegas";
            }
            
            // Verificar si tiene stock asociado
            boolean tieneStock = stockService.existsStockByBodegaId(id);
            if (tieneStock) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar la bodega porque tiene stock asociado. Traslade primero el stock a otra bodega.");
                return "redirect:/admin/almacenes/" + almacenId + "/bodegas";
            }
            
            // Eliminar la bodega
            bodegaService.deleteBodega(id);
            redirectAttributes.addFlashAttribute("mensaje", "Bodega eliminada correctamente");
            return "redirect:/admin/almacenes/" + almacenId + "/bodegas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la bodega: " + e.getMessage());
            return "redirect:/admin/almacenes";
        }
    }
    
    /**
     * Ver las ubicaciones de una bodega
     */
    @GetMapping("/{id}/ubicaciones")
    public String verUbicaciones(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        // Verificar si la bodega existe
        Optional<Bodega> bodegaOpt = bodegaService.getBodegaById(id);
        if (!bodegaOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "La bodega no existe");
            return "redirect:/admin/almacenes";
        }
        
        Bodega bodega = bodegaOpt.get();
        List<UbicacionBodega> ubicaciones = ubicacionService.getUbicacionesByBodega(id);
        
        model.addAttribute("bodega", bodega);
        model.addAttribute("ubicaciones", ubicaciones);
        
        return "admin/ubicaciones";
    }
}
