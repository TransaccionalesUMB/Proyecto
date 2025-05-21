package com.example.transactional.repository;

import com.example.transactional.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a los datos de Sucursal
 */
@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    
    /**
     * Busca una sucursal por su nombre
     * @param nombre Nombre de la sucursal
     * @return Sucursal encontrada o vacío
     */
    Optional<Sucursal> findByNombre(String nombre);
    
    /**
     * Busca sucursales por almacén
     * @param idAlmacen ID del almacén
     * @return Lista de sucursales del almacén
     */
    List<Sucursal> findByIdAlmacen(Integer idAlmacen);
    
    /**
     * Busca sucursales por dirección
     * @param direccion Dirección de la sucursal
     * @return Lista de sucursales con esa dirección
     */
    List<Sucursal> findByDireccionContaining(String direccion);
    
    /**
     * Obtiene todas las sucursales ordenadas por nombre
     * @return Lista de sucursales ordenadas
     */
    List<Sucursal> findAllByOrderByNombreAsc();
    
    /**
     * Obtiene sucursales con sus bodegas
     * @return Lista de sucursales con datos de bodegas
     */
    @Query("SELECT DISTINCT s FROM Sucursal s LEFT JOIN FETCH s.bodegas")
    List<Sucursal> findAllWithBodegas();
    
    /**
     * Obtiene sucursales con sus bodegas por almacén
     * @param idAlmacen ID del almacén
     * @return Lista de sucursales con datos de bodegas del almacén
     */
    @Query("SELECT DISTINCT s FROM Sucursal s LEFT JOIN FETCH s.bodegas WHERE s.idAlmacen = :idAlmacen")
    List<Sucursal> findByIdAlmacenWithBodegas(@Param("idAlmacen") Integer idAlmacen);
}
