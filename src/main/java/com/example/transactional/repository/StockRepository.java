package com.example.transactional.repository;

import com.example.transactional.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceder a los datos de Stock
 * Incluye métodos para buscar por producto, bodega, ubicación y lote
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    
    // Métodos de búsqueda por campos individuales
    List<Stock> findByIdProducto(Integer idProducto);
    List<Stock> findByIdBodega(Integer idBodega);
    
    // Alias para compatibilidad con nuevos controladores
    default List<Stock> findByProductoId(Integer productoId) {
        return findByIdProducto(productoId);
    }
    
    default List<Stock> findByBodegaId(Integer bodegaId) {
        return findByIdBodega(bodegaId);
    }
    
    // Métodos de búsqueda combinados
    List<Stock> findByIdProductoAndIdBodega(Integer idProducto, Integer idBodega);
    
    // Consultas personalizadas
    @Query("SELECT SUM(s.cantidad) FROM Stock s WHERE s.idProducto = :idProducto")
    Integer getTotalStockByProductId(@Param("idProducto") Integer idProducto);
    
    // Alias para compatibilidad con nuevos controladores
    default Integer sumCantidadByProductoId(Integer productoId) {
        return getTotalStockByProductId(productoId);
    }
    
    // Verificar existencia
    boolean existsByIdBodega(Integer idBodega);
    
    // Alias para compatibilidad con nuevos controladores
    default boolean existsByBodegaId(Integer bodegaId) {
        return existsByIdBodega(bodegaId);
    }
    
    @Query("SELECT SUM(s.cantidad) FROM Stock s WHERE s.idProducto = :idProducto AND s.idBodega = :idBodega")
    Integer getTotalStockByProductIdAndWarehouseId(@Param("idProducto") Integer idProducto, @Param("idBodega") Integer idBodega);
    
    // Compatibilidad con código existente (métodos antiguos que redirigen a los nuevos)
    default List<Stock> findByProductId(Integer productId) {
        return findByIdProducto(productId);
    }
}
