package com.example.transactional.repository;

import com.example.transactional.model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a los datos de Bodega
 */
@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Integer> {
    
    /**
     * Busca una bodega por su nombre
     * @param nombre Nombre de la bodega
     * @return Bodega encontrada o vacío
     */
    Optional<Bodega> findByNombre(String nombre);
    
    /**
     * Busca bodegas por sucursal
     * @param idSucursal ID de la sucursal
     * @return Lista de bodegas de la sucursal
     */
    List<Bodega> findByIdSucursal(Integer idSucursal);
    
    /**
     * Busca bodegas por almacén
     * @param idAlmacen ID del almacén
     * @return Lista de bodegas del almacén
     */
    List<Bodega> findByIdAlmacen(Integer idAlmacen);
    
    /**
     * Obtiene todas las bodegas ordenadas por nombre
     * @return Lista de bodegas ordenadas
     */
    List<Bodega> findAllByOrderByNombreAsc();
    
    /**
     * Obtiene bodegas con sus ubicaciones
     * @return Lista de bodegas con datos de ubicaciones
     */
    @Query("SELECT DISTINCT b FROM Bodega b LEFT JOIN FETCH b.ubicaciones")
    List<Bodega> findAllWithUbicaciones();
    
    /**
     * Obtiene bodegas con sus ubicaciones por sucursal
     * @param idSucursal ID de la sucursal
     * @return Lista de bodegas con datos de ubicaciones de la sucursal
     */
    @Query("SELECT DISTINCT b FROM Bodega b LEFT JOIN FETCH b.ubicaciones WHERE b.idSucursal = :idSucursal")
    List<Bodega> findByIdSucursalWithUbicaciones(@Param("idSucursal") Integer idSucursal);
    
    /**
     * Obtiene bodegas con stock disponible para un producto
     * @param idProducto ID del producto
     * @return Lista de bodegas con stock disponible del producto
     */
    @Query("SELECT DISTINCT b FROM Bodega b JOIN Stock s ON s.idBodega = b.id WHERE s.idProducto = :idProducto AND s.cantidad > 0")
    List<Bodega> findBodegasWithStockByProducto(@Param("idProducto") Integer idProducto);
}
