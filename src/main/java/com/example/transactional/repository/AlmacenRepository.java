package com.example.transactional.repository;

import com.example.transactional.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a los datos de Almacen
 */
@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
    
    /**
     * Busca un almacén por su nombre
     * @param nombre Nombre del almacén
     * @return Almacén encontrado o vacío
     */
    Optional<Almacen> findByNombre(String nombre);
    
    /**
     * Busca almacenes por dirección
     * @param direccion Dirección del almacén
     * @return Lista de almacenes en esa dirección
     */
    List<Almacen> findByDireccionContaining(String direccion);
    
    /**
     * Obtiene todos los almacenes ordenados por nombre
     * @return Lista de almacenes ordenados
     */
    List<Almacen> findAllByOrderByNombreAsc();
    
    /**
     * Obtiene almacenes con sus sucursales
     * @return Lista de almacenes con datos de sucursales
     */
    @Query("SELECT DISTINCT a FROM Almacen a LEFT JOIN FETCH a.sucursales")
    List<Almacen> findAllWithSucursales();
}
