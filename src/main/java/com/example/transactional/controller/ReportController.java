package com.example.transactional.controller;

import com.example.transactional.dto.ProductDto;
import com.example.transactional.service.ProductService;
import com.example.transactional.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ProductService productService;
    
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
        
        // Obtener datos del servicio
        Map<String, Integer> stockByCategory = reportService.getStockByCategory();
        Map<String, String> categoryNames = reportService.getCategoryNames();
        
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
        
        // Obtener datos del servicio
        Map<String, Integer> stockByProvider = reportService.getStockByProvider();
        Map<String, String> providerNames = reportService.getProviderNames();
        
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
