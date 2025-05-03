package com.example.transactional.service;

import com.example.transactional.dto.ProductDto;
import com.example.transactional.model.Product;
import com.example.transactional.model.Stock;
import com.example.transactional.repository.ProductRepository;
import com.example.transactional.repository.StockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    public List<ProductDto> getAllProductsWithStock() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        
        for (Product product : products) {
            ProductDto dto = convertToDto(product);
            Integer totalStock = stockRepository.getTotalStockByProductId(product.getId());
            dto.setStock(totalStock != null ? totalStock : 0);
            productDtos.add(dto);
        }
        
        return productDtos;
    }
    
    @Transactional
    public Product saveProductWithStock(ProductDto productDto) {
        // Guardar el producto
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategoryId(productDto.getCategoryId());
        product.setProviderId(productDto.getProviderId());
        
        Product savedProduct = productRepository.save(product);
        
        // Si se especifica stock, crear un registro en la tabla Stock
        if (productDto.getStock() != null && productDto.getStock() > 0) {
            Stock stock = new Stock();
            stock.setProductId(savedProduct.getId());
            stock.setProductName(savedProduct.getName());
            stock.setQuantity(productDto.getStock());
            stock.setWarehouseId(1); // Valor predeterminado
            
            stockRepository.save(stock);
        }
        
        return savedProduct;
    }
    
    public Optional<ProductDto> getProductById(Integer id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            ProductDto dto = convertToDto(productOpt.get());
            Integer totalStock = stockRepository.getTotalStockByProductId(id);
            dto.setStock(totalStock != null ? totalStock : 0);
            return Optional.of(dto);
        }
        return Optional.empty();
    }
    
    @Transactional
    public Product updateProduct(ProductDto productDto) {
        // Buscar y actualizar el producto existente
        Optional<Product> existingProductOpt = productRepository.findById(productDto.getId());
        if (existingProductOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado con ID: " + productDto.getId());
        }
        
        Product existingProduct = existingProductOpt.get();
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setCategoryId(productDto.getCategoryId());
        existingProduct.setProviderId(productDto.getProviderId());
        
        Product updatedProduct = productRepository.save(existingProduct);
        
        // Actualizar stock si ha cambiado
        Integer currentStock = stockRepository.getTotalStockByProductId(updatedProduct.getId());
        Integer newStock = productDto.getStock();
        
        if (newStock != null && (currentStock == null || !newStock.equals(currentStock))) {
            List<Stock> stocks = stockRepository.findByProductId(updatedProduct.getId());
            
            if (stocks.isEmpty()) {
                // No hay registros de stock, crear uno nuevo si el stock es mayor que cero
                if (newStock > 0) {
                    Stock stock = new Stock();
                    stock.setProductId(updatedProduct.getId());
                    stock.setProductName(updatedProduct.getName());
                    stock.setQuantity(newStock);
                    stock.setWarehouseId(1); // Valor predeterminado
                    stockRepository.save(stock);
                }
            } else {
                // Actualizar el stock existente (primer registro)
                Stock stock = stocks.get(0);
                stock.setProductName(updatedProduct.getName());
                stock.setQuantity(newStock);
                stockRepository.save(stock);
                
                // Si hay múltiples registros de stock, eliminar los adicionales para mantener consistencia
                if (stocks.size() > 1) {
                    for (int i = 1; i < stocks.size(); i++) {
                        stockRepository.delete(stocks.get(i));
                    }
                }
            }
        }
        
        return updatedProduct;
    }
    
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategoryId());
        dto.setProviderId(product.getProviderId());
        return dto;
    }
}
