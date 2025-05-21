package com.example.transactional.repository;

import com.example.transactional.model.UbicacionBodega;
import com.example.transactional.model.UbicacionBodega.TipoUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a los datos de UbicacionBodega
 */
@Repository
public interface UbicacionBodegaRepository extends JpaRepository<UbicacionBodega, Integer> {
    
    /**
     * Busca ubicaciones por bodega
     * @param idBodega ID de la bodega
     * @return Lista de ubicaciones de la bodega
     */
    List<UbicacionBodega> findByIdBodega(Integer idBodega);
    
    /**
     * Busca ubicaciones activas por bodega
     * @param idBodega ID de la bodega
     * @return Lista de ubicaciones activas de la bodega
     */
    List<UbicacionBodega> findByIdBodegaAndActivaTrue(Integer idBodega);
    
    /**
     * Busca ubicaciones por código
     * @param codigo Código de la ubicación
     * @return Lista de ubicaciones con ese código
     */
    List<UbicacionBodega> findByCodigoContaining(String codigo);
    
    /**
     * Busca ubicaciones por tipo
     * @param tipo Tipo de ubicación
     * @return Lista de ubicaciones de ese tipo
     */
    List<UbicacionBodega> findByTipo(TipoUbicacion tipo);
    
    /**
     * Busca ubicaciones por bodega y tipo
     * @param idBodega ID de la bodega
     * @param tipo Tipo de ubicación
     * @return Lista de ubicaciones de la bodega y tipo
     */
    List<UbicacionBodega> findByIdBodegaAndTipo(Integer idBodega, TipoUbicacion tipo);
    
    /**
     * Busca una ubicación específica en una bodega
     * @param idBodega ID de la bodega
     * @param codigo Código de la ubicación
     * @return Ubicación encontrada o vacío
     */
    Optional<UbicacionBodega> findByIdBodegaAndCodigo(Integer idBodega, String codigo);
    
    /**
     * Obtiene ubicaciones con stock disponible para un producto
     * @param idProducto ID del producto
     * @return Lista de ubicaciones disponibles para almacenamiento (ya no filtra por stock disponible)
     * @deprecated La relación entre Stock y UbicacionBodega ya no existe
     */
    @Deprecated
    @Query("SELECT u FROM UbicacionBodega u WHERE u.activa = true AND u.tipo = com.example.transactional.model.UbicacionBodega$TipoUbicacion.Almacenamiento")
    List<UbicacionBodega> findUbicacionesWithStockByProducto(@Param("idProducto") Integer idProducto);
    
    /**
     * Obtiene ubicaciones disponibles para almacenamiento en una bodega
     * @param idBodega ID de la bodega
     * @return Lista de ubicaciones disponibles para almacenamiento
     */
    @Query("SELECT u FROM UbicacionBodega u WHERE u.idBodega = :idBodega AND u.activa = true AND u.tipo = com.example.transactional.model.UbicacionBodega$TipoUbicacion.Almacenamiento")
    List<UbicacionBodega> findUbicacionesDisponiblesParaAlmacenamiento(@Param("idBodega") Integer idBodega);
}
