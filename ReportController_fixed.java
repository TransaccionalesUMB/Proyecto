package com.example.transactional.controller;

import com.example.transactional.dto.ProductDto;
import com.example.transactional.model.Category;
import com.example.transactional.model.Product;
import com.example.transactional.model.Provider;
import com.example.transactional.model.Stock;
import com.example.transactional.repository.CategoryRepository;
import com.example.transactional.repository.ProductRepository;
import com.example.transactional.repository.ProviderRepository;
import com.example.transactional.repository.StockRepository;
import com.example.transactional.service.ProductService;
import com.example.transactional.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProviderRepository providerRepository;
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping
    public String showReportsDashboard(Model model, Authentication authentication) {
        // Añadir atributos de navegación al modelo
        addNavigationAttributes(model, authentication);
        
        return "reports/dashboard";
    }
    
    @GetMapping("/stock-by-category")
    public String showStockByCategory(Model model, Authentication authentication) {
        List<ProductDto> products = productService.getAllProductsWithStock();
        
        // Preparar datos para el gráfico
        Map<Integer, Integer> stockByCategory = new HashMap<>();
        Map<Integer, String> categoryNames = new HashMap<>();
        
        // Obtener todas las categorías
        categoryRepository.findAll().forEach(category -> {
            categoryNames.put(category.getId(), category.getName());
            stockByCategory.put(category.getId(), 0);
        });
        
        // Calcular stock por categoría
        for (ProductDto product : products) {
            Integer categoryId = product.getCategoryId();
            if (categoryId != null && stockByCategory.containsKey(categoryId)) {
                int currentStock = stockByCategory.get(categoryId);
                stockByCategory.put(categoryId, currentStock + (product.getStock() != null ? product.getStock() : 0));
            }
        }
        
        // Añadir atributos de navegación al modelo
        addNavigationAttributes(model, authentication);
        
        // Agregar datos al modelo
        model.addAttribute("stockByCategory", stockByCategory);
        model.addAttribute("categoryNames", categoryNames);
        model.addAttribute("products", products);
        
        return "reports/stock-by-category";
    }
    
    @GetMapping("/stock-by-provider")
    public String showStockByProvider(Model model, Authentication authentication) {
        List<ProductDto> products = productService.getAllProductsWithStock();
        
        // Preparar datos para el gráfico
        Map<Integer, Integer> stockByProvider = new HashMap<>();
        Map<Integer, String> providerNames = new HashMap<>();
        
        // Obtener todos los proveedores
        providerRepository.findAll().forEach(provider -> {
            providerNames.put(provider.getId(), provider.getName());
            stockByProvider.put(provider.getId(), 0);
        });
        
        // Calcular stock por proveedor
        for (ProductDto product : products) {
            Integer providerId = product.getProviderId();
            if (providerId != null && stockByProvider.containsKey(providerId)) {
                int currentStock = stockByProvider.get(providerId);
                stockByProvider.put(providerId, currentStock + (product.getStock() != null ? product.getStock() : 0));
            }
        }
        
        // Añadir atributos de navegación al modelo
        addNavigationAttributes(model, authentication);
        
        // Agregar datos al modelo
        model.addAttribute("stockByProvider", stockByProvider);
        model.addAttribute("providerNames", providerNames);
        model.addAttribute("products", products);
        
        return "reports/stock-by-provider";
    }
    
    private void addNavigationAttributes(Model model, Authentication authentication) {
        model.addAttribute("isAdmin", true);
        model.addAttribute("canViewInventory", true);
        model.addAttribute("canViewReports", true);
        model.addAttribute("canViewProducts", true);
        model.addAttribute("canViewUsers", true);
    }
}
