package com.example.transactional.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.transactional.dto.ProductDto;
import com.example.transactional.model.Category;
import com.example.transactional.model.Provider;
import com.example.transactional.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    // Usar listas en memoria en lugar de repositorios para evitar problemas con la base de datos
    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        
        Category general = new Category();
        general.setId(1);
        general.setName("Categoría General");
        general.setDescription("Categoría por defecto para productos");
        categories.add(general);
        
        Category electronics = new Category();
        electronics.setId(2);
        electronics.setName("Electrónicos");
        electronics.setDescription("Productos electrónicos y gadgets");
        categories.add(electronics);
        
        Category office = new Category();
        office.setId(3);
        office.setName("Oficina");
        office.setDescription("Artículos de oficina y papelería");
        categories.add(office);
        
        return categories;
    }
    
    private List<Provider> getProviders() {
        List<Provider> providers = new ArrayList<>();
        
        Provider general = new Provider();
        general.setId(1);
        general.setName("Proveedor General");
        general.setDescription("Proveedor por defecto");
        providers.add(general);
        
        Provider tech = new Provider();
        tech.setId(2);
        tech.setName("TechSupplies Inc.");
        tech.setDescription("Proveedor de equipos electrónicos");
        providers.add(tech);
        
        Provider office = new Provider();
        office.setId(3);
        office.setName("Office Solutions");
        office.setDescription("Proveedor de artículos de oficina");
        providers.add(office);
        
        return providers;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProductsWithStock());
        model.addAttribute("newProduct", new ProductDto());
        model.addAttribute("categories", getCategories());
        model.addAttribute("providers", getProviders());
        return "products/list";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductDto productDto, RedirectAttributes redirectAttributes) {
        try {
            // Validar que los campos requeridos estén presentes
            if (productDto.getName() == null || productDto.getName().trim().isEmpty()) {
                throw new RuntimeException("El nombre del producto es obligatorio");
            }
            
            if (productDto.getPrice() == null) {
                throw new RuntimeException("El precio del producto es obligatorio");
            }
            
            productService.saveProductWithStock(productDto);
            redirectAttributes.addFlashAttribute("success", "Producto creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el producto: " + e.getMessage());
        }
        return "redirect:/products";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<ProductDto> productOpt = productService.getProductById(id);
            if (productOpt.isPresent()) {
                model.addAttribute("product", productOpt.get());
                model.addAttribute("categories", getCategories());
                model.addAttribute("providers", getProviders());
                return "products/edit";
            } else {
                redirectAttributes.addFlashAttribute("error", "No se encontró el producto con ID: " + id);
                return "redirect:/products";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al obtener el producto: " + e.getMessage());
            return "redirect:/products";
        }
    }
    
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute ProductDto productDto, RedirectAttributes redirectAttributes) {
        try {
            productService.updateProduct(productDto);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto: " + e.getMessage());
        }
        return "redirect:/products";
    }
}
