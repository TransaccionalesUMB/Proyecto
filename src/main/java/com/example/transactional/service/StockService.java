package com.example.transactional.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transactional.model.Stock;
import com.example.transactional.repository.StockRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de stock
 */
@Service
@Transactional
public class StockService {
    
    @Autowired
    private StockRepository stockRepository;
    
    /**
     * Obtener todos los registros de stock
     */
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }
    
    /**
     * Obtener un registro de stock por su ID
     */
    public Optional<Stock> getStockById(Integer id) {
        return stockRepository.findById(id);
    }
    
    /**
     * Obtener un registro de stock por su ID (String)
     */
    public Optional<Stock> getStockById(String id) {
        try {
            return getStockById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Guardar un nuevo registro de stock
     */
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }
    
    /**
     * Actualizar un registro de stock existente
     */
    public Stock updateStock(Stock stock) {
        return stockRepository.save(stock);
    }
    
    /**
     * Eliminar un registro de stock
     */
    public void deleteStock(Integer id) {
        stockRepository.deleteById(id);
    }
    
    /**
     * Verificar si existe stock asociado a una bodega
     */
    public boolean existsStockByBodegaId(Integer bodegaId) {
        return stockRepository.existsByBodegaId(bodegaId);
    }
    
    /**
     * Verificar si existe stock asociado a una ubicación
     * @deprecated El modelo Stock ya no tiene relación con ubicaciones
     */
    @Deprecated
    public boolean existsStockByUbicacionId(Integer ubicacionId) {
        System.err.println("Método obsoleto: existsStockByUbicacionId. El modelo Stock ya no tiene relación con ubicaciones.");
        return false;
    }
    
    /**
     * Obtener stock por producto
     */
    public List<Stock> getStockByProductoId(Integer productoId) {
        return stockRepository.findByProductoId(productoId);
    }
    
    /**
     * Obtener stock por bodega
     */
    public List<Stock> getStockByBodegaId(Integer bodegaId) {
        return stockRepository.findByBodegaId(bodegaId);
    }
    
    /**
     * Obtener stock por ubicación
     * @deprecated El modelo Stock ya no tiene relación con ubicaciones
     */
    @Deprecated
    public List<Stock> getStockByUbicacionId(Integer ubicacionId) {
        System.err.println("Método obsoleto: getStockByUbicacionId. El modelo Stock ya no tiene relación con ubicaciones.");
        return java.util.Collections.emptyList();
    }
    
    /**
     * Obtener stock por lote
     * @deprecated El modelo Stock ya no tiene relación con lotes
     */
    @Deprecated
    public List<Stock> getStockByLoteId(Integer loteId) {
        System.err.println("Método obsoleto: getStockByLoteId. El modelo Stock ya no tiene relación con lotes.");
        return java.util.Collections.emptyList();
    }
    
    /**
     * Obtener la cantidad total de un producto en stock
     */
    public Integer getTotalStockByProductoId(Integer productoId) {
        return stockRepository.sumCantidadByProductoId(productoId);
    }
}
