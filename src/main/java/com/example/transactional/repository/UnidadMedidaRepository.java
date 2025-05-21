package com.example.transactional.repository;

import com.example.transactional.model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a los datos de UnidadMedida
 */
@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer> {
    
    /**
     * Busca una unidad de medida por su nombre
     * @param nombre Nombre de la unidad de medida
     * @return Unidad de medida encontrada o vacío
     */
    Optional<UnidadMedida> findByNombre(String nombre);
    
    /**
     * Busca una unidad de medida por su abreviatura
     * @param abreviatura Abreviatura de la unidad de medida
     * @return Unidad de medida encontrada o vacío
     */
    Optional<UnidadMedida> findByAbreviatura(String abreviatura);
    
    /**
     * Busca todas las unidades de medida base
     * @return Lista de unidades de medida base
     */
    List<UnidadMedida> findByEsUnidadBaseTrue();
}
