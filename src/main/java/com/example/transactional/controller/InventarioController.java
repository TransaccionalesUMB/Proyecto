package com.example.transactional.controller;

import com.example.transactional.model.*;
import com.example.transactional.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para la gestión avanzada de inventario
 */
@Controller
@RequestMapping("/inventario")
public class InventarioController {
    
    @Autowired
    private InventarioService inventarioService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private LoteService loteService;
    
    @Autowired
    private AlmacenService almacenService;
    
    // Eliminamos la inyección de SucursalService ya que no se utiliza directamente
    
    @Autowired
    private BodegaService bodegaService;
    
    @Autowired
    private UbicacionBodegaService ubicacionService;
    
    /**
     * Página principal de gestión de inventario
     */
    @GetMapping
    public String index(Model model) {
        try {
            // Obtener datos para mostrar en la página principal
            List<Stock> stocks = inventarioService.getAllStock();
            List<Almacen> almacenes = almacenService.getAllAlmacenes();
            
            model.addAttribute("stocks", stocks);
            model.addAttribute("almacenes", almacenes);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "inventario/index";
        } catch (org.hibernate.exception.SQLGrammarException e) {
            // Manejar específicamente errores de SQL
            model.addAttribute("error", "Hubo un problema al cargar los datos del inventario: could not extract ResultSet. SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not extract ResultSet");
            
            // Proporcionar listas vacías para evitar NullPointerException en la vista
            model.addAttribute("stocks", java.util.Collections.emptyList());
            model.addAttribute("almacenes", java.util.Collections.emptyList());
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "inventario/index";
        } catch (Exception e) {
            // Capturar la excepción y mostrar un mensaje de error amigable
            String errorMsg = "Hubo un problema al cargar los datos del inventario";
            
            // Si es un error SQL, proporcionar un mensaje más específico
            if (e.getMessage() != null && e.getMessage().contains("SQL")) {
                errorMsg += ": could not extract ResultSet. Esto puede deberse a un problema con la estructura de la base de datos o tipos de datos incompatibles.";
            } else {
                errorMsg += ": " + e.getMessage();
            }
            
            // Proporcionar listas vacías para evitar NullPointerException en la vista
            model.addAttribute("stocks", java.util.Collections.emptyList());
            model.addAttribute("almacenes", java.util.Collections.emptyList());
            model.addAttribute("error", errorMsg);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "inventario/index";
        }
    }
    
    /**
     * Página para ver el stock de un producto específico
     */
    @GetMapping("/producto/{id}")
    public String verStockProducto(@PathVariable Integer id, Model model) {
        // Obtener el producto
        Optional<Product> productoOpt = productService.getProductEntityById(id);
        
        if (!productoOpt.isPresent()) {
            return "redirect:/inventario?error=Producto+no+encontrado";
        }
        
        Product producto = productoOpt.get();
        List<Stock> stocks = inventarioService.getStockByProducto(id);
        List<Lote> lotes = loteService.getLotesByProducto(id);
        
        model.addAttribute("producto", producto);
        model.addAttribute("stocks", stocks);
        model.addAttribute("lotes", lotes);
        
        return "inventario/producto";
    }
    
    /**
     * Página para ver el stock en una bodega específica
     */
    @GetMapping("/bodega/{id}")
    public String verStockBodega(@PathVariable Integer id, Model model) {
        // Obtener la bodega
        Optional<Bodega> bodegaOpt = bodegaService.getBodegaById(id);
        
        if (!bodegaOpt.isPresent()) {
            return "redirect:/inventario?error=Bodega+no+encontrada";
        }
        
        Bodega bodega = bodegaOpt.get();
        List<Stock> stocks = inventarioService.getStockByBodega(id);
        List<UbicacionBodega> ubicaciones = ubicacionService.getUbicacionesByBodega(id);
        
        model.addAttribute("bodega", bodega);
        model.addAttribute("stocks", stocks);
        model.addAttribute("ubicaciones", ubicaciones);
        
        return "inventario/bodega";
    }
    
    /**
     * Formulario para registrar entrada de stock
     */
    @GetMapping("/entradas")
    public String formularioEntrada(Model model) {
        try {
            List<Product> productos = productService.getAllProducts();
            List<Bodega> bodegas = bodegaService.getAllBodegas();
            
            model.addAttribute("productos", productos);
            model.addAttribute("bodegas", bodegas);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "inventario/entrada";
        } catch (org.hibernate.exception.SQLGrammarException e) {
            // Manejar específicamente errores de SQL
            model.addAttribute("error", "Error al cargar el formulario de entrada: could not extract ResultSet. SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not extract ResultSet");
            
            // Proporcionar listas vacías para evitar NullPointerException en la vista
            model.addAttribute("productos", java.util.Collections.emptyList());
            model.addAttribute("bodegas", java.util.Collections.emptyList());
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "error";
        } catch (Exception e) {
            // Capturar otras excepciones
            String errorMsg = "Error al cargar el formulario de entrada";
            
            if (e.getMessage() != null) {
                errorMsg += ": " + e.getMessage();
            }
            
            model.addAttribute("error", errorMsg);
            
            // Proporcionar listas vacías para evitar NullPointerException en la vista
            model.addAttribute("productos", java.util.Collections.emptyList());
            model.addAttribute("bodegas", java.util.Collections.emptyList());
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "error";
        }
    }
    
    /**
     * Procesar entrada de stock
     */
    @PostMapping("/entradas")
    public String procesarEntrada(
            @RequestParam Integer idProducto,
            @RequestParam Integer idBodega,
            @RequestParam(required = false) Integer idUbicacion,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) String numeroLote,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaCaducidad,
            RedirectAttributes redirectAttributes) {
        
        try {
            inventarioService.registrarEntrada(idProducto, idBodega, idUbicacion, cantidad, numeroLote, fechaCaducidad);
            redirectAttributes.addFlashAttribute("mensaje", "Entrada de stock registrada correctamente");
            return "redirect:/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar entrada: " + e.getMessage());
            return "redirect:/inventario/entradas";
        }
    }
    
    /**
     * Formulario para registrar salida de stock
     */
    @GetMapping("/salidas")
    public String formularioSalida(Model model) {
        try {
            List<Product> productos = productService.getAllProducts();
            List<Bodega> bodegas = bodegaService.getAllBodegas();
            
            model.addAttribute("productos", productos);
            model.addAttribute("bodegas", bodegas);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "inventario/salida";
        } catch (org.hibernate.exception.SQLGrammarException e) {
            // Manejar específicamente errores de SQL
            model.addAttribute("error", "Error al cargar el formulario de salida: could not extract ResultSet. SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not extract ResultSet");
            
            // Proporcionar listas vacías para evitar NullPointerException en la vista
            model.addAttribute("productos", java.util.Collections.emptyList());
            model.addAttribute("bodegas", java.util.Collections.emptyList());
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "error";
        } catch (Exception e) {
            // Capturar otras excepciones
            String errorMsg = "Error al cargar el formulario de salida";
            
            if (e.getMessage() != null) {
                errorMsg += ": " + e.getMessage();
            }
            
            model.addAttribute("error", errorMsg);
            
            // Proporcionar listas vacías para evitar NullPointerException en la vista
            model.addAttribute("productos", java.util.Collections.emptyList());
            model.addAttribute("bodegas", java.util.Collections.emptyList());
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "error";
        }
    }
    
    /**
     * Procesar salida de stock
     */
    @PostMapping("/salidas")
    public String procesarSalida(
            @RequestParam Integer idProducto,
            @RequestParam Integer idBodega,
            @RequestParam(required = false) Integer idUbicacion,
            @RequestParam(required = false) Integer idLote,
            @RequestParam Integer cantidad,
            RedirectAttributes redirectAttributes) {
        
        try {
            inventarioService.registrarSalida(idProducto, idBodega, idUbicacion, idLote, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Salida de stock registrada correctamente");
            return "redirect:/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar salida: " + e.getMessage());
            return "redirect:/inventario/salidas";
        }
    }
    
    /**
     * Formulario para transferir stock
     */
    @GetMapping("/movimientos")
    public String formularioTransferencia(Model model) {
        try {
            List<Product> productos = productService.getAllProducts();
            List<Bodega> bodegas = bodegaService.getAllBodegas();
            
            model.addAttribute("productos", productos);
            model.addAttribute("bodegas", bodegas);
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "inventario/transferencia";
        } catch (org.hibernate.exception.SQLGrammarException e) {
            // Manejar específicamente errores de SQL
            model.addAttribute("error", "Error al cargar el formulario de transferencia: could not extract ResultSet. SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not extract ResultSet");
            
            // Proporcionar listas vacías para evitar NullPointerException en la vista
            model.addAttribute("productos", java.util.Collections.emptyList());
            model.addAttribute("bodegas", java.util.Collections.emptyList());
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "error";
        } catch (Exception e) {
            // Capturar otras excepciones
            String errorMsg = "Error al cargar el formulario de transferencia";
            
            if (e.getMessage() != null) {
                errorMsg += ": " + e.getMessage();
            }
            
            model.addAttribute("error", errorMsg);
            
            // Proporcionar listas vacías para evitar NullPointerException en la vista
            model.addAttribute("productos", java.util.Collections.emptyList());
            model.addAttribute("bodegas", java.util.Collections.emptyList());
            
            // Añadir atributos para la navegación
            model.addAttribute("isAdmin", true);
            model.addAttribute("canViewInventory", true);
            model.addAttribute("canViewReports", true);
            model.addAttribute("canViewProducts", true);
            model.addAttribute("canViewUsers", true);
            
            return "error";
        }
    }
    
    /**
     * Procesar transferencia de stock
     */
    @PostMapping("/movimientos")
    public String procesarTransferencia(
            @RequestParam Integer idProducto,
            @RequestParam Integer idBodegaOrigen,
            @RequestParam(required = false) Integer idUbicacionOrigen,
            @RequestParam(required = false) Integer idLote,
            @RequestParam Integer idBodegaDestino,
            @RequestParam(required = false) Integer idUbicacionDestino,
            @RequestParam Integer cantidad,
            RedirectAttributes redirectAttributes) {
        
        try {
            inventarioService.transferirStock(idProducto, idBodegaOrigen, idUbicacionOrigen, 
                                             idLote, idBodegaDestino, idUbicacionDestino, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Transferencia de stock realizada correctamente");
            return "redirect:/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al realizar transferencia: " + e.getMessage());
            return "redirect:/inventario/movimientos";
        }
    }
    
    /**
     * Página para ver lotes próximos a caducar
     */
    @GetMapping("/lotes/proximos-caducar")
    public String verLotesProximosCaducar(
            @RequestParam(defaultValue = "30") Integer diasLimite,
            Model model) {
        
        List<Lote> lotes = loteService.getLotesProximosACaducar();
        
        model.addAttribute("lotes", lotes);
        model.addAttribute("diasLimite", diasLimite);
        
        return "inventario/lotes-proximos-caducar";
    }
    
    /**
     * Obtener ubicaciones de una bodega (para AJAX)
     */
    @GetMapping("/api/ubicaciones/{idBodega}")
    @ResponseBody
    public List<UbicacionBodega> getUbicacionesByBodega(@PathVariable Integer idBodega) {
        return ubicacionService.getUbicacionesByBodega(idBodega);
    }
    
    /**
     * Obtener lotes de un producto (para AJAX)
     */
    @GetMapping("/api/lotes/{idProducto}")
    @ResponseBody
    public List<Lote> getLotesByProducto(@PathVariable Integer idProducto) {
        return loteService.getLotesByProducto(idProducto);
    }
}
