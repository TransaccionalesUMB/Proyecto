package com.example.transactional.service;

import com.example.transactional.dto.ProductDto;
import com.example.transactional.repository.CategoryRepository;
import com.example.transactional.repository.ProviderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProviderRepository providerRepository;
    
    // No se necesita el StockRepository ya que obtenemos el stock desde ProductService
    
    /**
     * Obtiene el stock agrupado por categoría
     * @return Mapa con el ID de categoría como clave y el stock total como valor
     */
    public Map<String, Integer> getStockByCategory() {
        List<ProductDto> products = productService.getAllProductsWithStock();
        Map<String, Integer> stockByCategory = new HashMap<>();
        
        // Inicializar el mapa con todas las categorías
        categoryRepository.findAll().forEach(category -> {
            stockByCategory.put(category.getId(), 0);
        });
        
        // Calcular stock por categoría
        for (ProductDto product : products) {
            String categoryId = product.getCategoryId();
            if (categoryId != null && stockByCategory.containsKey(categoryId)) {
                int currentStock = stockByCategory.get(categoryId);
                stockByCategory.put(categoryId, currentStock + (product.getStock() != null ? product.getStock() : 0));
            }
        }
        
        return stockByCategory;
    }
    
    /**
     * Obtiene los nombres de las categorías
     * @return Mapa con el ID de categoría como clave y el nombre como valor
     */
    public Map<String, String> getCategoryNames() {
        Map<String, String> categoryNames = new HashMap<>();
        categoryRepository.findAll().forEach(category -> {
            categoryNames.put(category.getId(), category.getName());
        });
        return categoryNames;
    }
    
    /**
     * Obtiene el stock agrupado por proveedor
     * @return Mapa con el ID de proveedor como clave y el stock total como valor
     */
    public Map<String, Integer> getStockByProvider() {
        List<ProductDto> products = productService.getAllProductsWithStock();
        Map<String, Integer> stockByProvider = new HashMap<>();
        
        // Inicializar el mapa con todos los proveedores
        providerRepository.findAll().forEach(provider -> {
            stockByProvider.put(provider.getId(), 0);
        });
        
        // Calcular stock por proveedor
        for (ProductDto product : products) {
            String providerId = product.getProviderId();
            if (providerId != null && stockByProvider.containsKey(providerId)) {
                int currentStock = stockByProvider.get(providerId);
                stockByProvider.put(providerId, currentStock + (product.getStock() != null ? product.getStock() : 0));
            }
        }
        
        return stockByProvider;
    }
    
    /**
     * Obtiene los nombres de los proveedores
     * @return Mapa con el ID de proveedor como clave y el nombre como valor
     */
    public Map<String, String> getProviderNames() {
        Map<String, String> providerNames = new HashMap<>();
        providerRepository.findAll().forEach(provider -> {
            providerNames.put(provider.getId(), provider.getName());
        });
        return providerNames;
    }
}
