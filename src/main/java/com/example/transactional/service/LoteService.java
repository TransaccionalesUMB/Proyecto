package com.example.transactional.service;

import com.example.transactional.model.Lote;
import com.example.transactional.model.Product;
import com.example.transactional.repository.LoteRepository;
import com.example.transactional.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar los lotes de productos
 */
@Service
public class LoteService {
    
    @Autowired
    private LoteRepository loteRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Obtiene todos los lotes
     * @return Lista de lotes
     */
    public List<Lote> getAllLotes() {
        return loteRepository.findAll();
    }
    
    /**
     * Obtiene un lote por su ID
     * @param id ID del lote
     * @return Lote encontrado o vacío
     */
    public Optional<Lote> getLoteById(Integer id) {
        return loteRepository.findById(id);
    }
    
    /**
     * Obtiene lotes por producto
     * @param idProducto ID del producto
     * @return Lista de lotes del producto
     */
    public List<Lote> getLotesByProducto(Integer idProducto) {
        return loteRepository.findByIdProducto(idProducto);
    }
    
    /**
     * Obtiene lotes por número de lote
     * @param numeroLote Número de lote
     * @return Lista de lotes con ese número
     */
    public List<Lote> getLotesByNumeroLote(String numeroLote) {
        return loteRepository.findByNumeroLote(numeroLote);
    }
    
    /**
     * Obtiene lotes próximos a caducar (en los próximos 30 días)
     * @return Lista de lotes próximos a caducar
     */
    public List<Lote> getLotesProximosACaducar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date fechaLimite = calendar.getTime();
        
        return loteRepository.findByFechaCaducidadBefore(fechaLimite);
    }
    
    /**
     * Obtiene lotes con stock disponible
     * @return Lista de lotes con stock disponible
     */
    public List<Lote> getLotesConStock() {
        return loteRepository.findLotesConStock();
    }
    
    /**
     * Guarda un lote
     * @param lote Lote a guardar
     * @return Lote guardado
     */
    public Lote saveLote(Lote lote) {
        // Verificar que el producto existe
        Optional<Product> producto = productRepository.findById(lote.getIdProducto());
        if (!producto.isPresent()) {
            throw new IllegalArgumentException("El producto con ID " + lote.getIdProducto() + " no existe");
        }
        
        // Si es un lote nuevo, la cantidad actual debe ser igual a la cantidad inicial
        if (lote.getId() == null) {
            lote.setCantidadActual(lote.getCantidadInicial());
        }
        
        return loteRepository.save(lote);
    }
    
    /**
     * Actualiza la cantidad actual de un lote
     * @param idLote ID del lote
     * @param cantidad Nueva cantidad
     * @return Lote actualizado o vacío si no existe
     */
    public Optional<Lote> actualizarCantidad(Integer idLote, Integer cantidad) {
        Optional<Lote> loteOpt = loteRepository.findById(idLote);
        
        if (loteOpt.isPresent()) {
            Lote lote = loteOpt.get();
            
            // Verificar que la cantidad no sea negativa
            if (cantidad < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa");
            }
            
            // Verificar que la cantidad no sea mayor que la cantidad inicial
            if (cantidad > lote.getCantidadInicial()) {
                throw new IllegalArgumentException("La cantidad no puede ser mayor que la cantidad inicial");
            }
            
            lote.setCantidadActual(cantidad);
            return Optional.of(loteRepository.save(lote));
        }
        
        return Optional.empty();
    }
    
    /**
     * Elimina un lote
     * @param id ID del lote a eliminar
     */
    public void deleteLote(Integer id) {
        loteRepository.deleteById(id);
    }
    
    /**
     * Verifica si un lote está próximo a caducar
     * @param idLote ID del lote
     * @return true si está próximo a caducar, false en caso contrario
     */
    public boolean isLoteProximoACaducar(Integer idLote) {
        Optional<Lote> loteOpt = loteRepository.findById(idLote);
        
        if (loteOpt.isPresent()) {
            Lote lote = loteOpt.get();
            return lote.isProximoACaducar();
        }
        
        return false;
    }
    
    /**
     * Verifica si un lote ha caducado
     * @param idLote ID del lote
     * @return true si ha caducado, false en caso contrario
     */
    public boolean isLoteCaducado(Integer idLote) {
        Optional<Lote> loteOpt = loteRepository.findById(idLote);
        
        if (loteOpt.isPresent()) {
            Lote lote = loteOpt.get();
            return lote.isCaducado();
        }
        
        return false;
    }
}
