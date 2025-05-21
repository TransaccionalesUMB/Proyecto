package com.example.transactional.repository;

import com.example.transactional.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio para acceder a los datos de Lote
 */
@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    
    /**
     * Busca lotes por producto
     * @param idProducto ID del producto
     * @return Lista de lotes del producto
     */
    List<Lote> findByIdProducto(Integer idProducto);
    
    /**
     * Busca lotes por número de lote
     * @param numeroLote Número de lote
     * @return Lista de lotes con ese número
     */
    List<Lote> findByNumeroLote(String numeroLote);
    
    /**
     * Busca lotes por proveedor
     * @param idProveedor ID del proveedor
     * @return Lista de lotes del proveedor
     */
    List<Lote> findByIdProveedor(String idProveedor);
    
    /**
     * Busca lotes próximos a caducar
     * @param fechaLimite Fecha límite para considerar próximos a caducar
     * @return Lista de lotes próximos a caducar
     */
    List<Lote> findByFechaCaducidadBefore(Date fechaLimite);
    
    /**
     * Busca lotes con stock disponible
     * @return Lista de lotes con stock disponible
     */
    @Query("SELECT l FROM Lote l WHERE l.cantidadActual > 0")
    List<Lote> findLotesConStock();
    
    /**
     * Busca lotes próximos a caducar de un producto específico
     * @param idProducto ID del producto
     * @param fechaLimite Fecha límite para considerar próximos a caducar
     * @return Lista de lotes próximos a caducar del producto
     */
    @Query("SELECT l FROM Lote l WHERE l.idProducto = :idProducto AND l.fechaCaducidad <= :fechaLimite AND l.cantidadActual > 0")
    List<Lote> findLotesProximosACaducarPorProducto(@Param("idProducto") Integer idProducto, @Param("fechaLimite") Date fechaLimite);
}
