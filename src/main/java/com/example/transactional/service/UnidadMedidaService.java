package com.example.transactional.service;

import com.example.transactional.model.UnidadMedida;
import com.example.transactional.repository.UnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las unidades de medida
 */
@Service
public class UnidadMedidaService {
    
    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;
    
    /**
     * Obtiene todas las unidades de medida
     * @return Lista de unidades de medida
     */
    public List<UnidadMedida> getAllUnidadesMedida() {
        return unidadMedidaRepository.findAll();
    }
    
    /**
     * Obtiene una unidad de medida por su ID
     * @param id ID de la unidad de medida
     * @return Unidad de medida encontrada o vacío
     */
    public Optional<UnidadMedida> getUnidadMedidaById(Integer id) {
        return unidadMedidaRepository.findById(id);
    }
    
    /**
     * Obtiene una unidad de medida por su nombre
     * @param nombre Nombre de la unidad de medida
     * @return Unidad de medida encontrada o vacío
     */
    public Optional<UnidadMedida> getUnidadMedidaByNombre(String nombre) {
        return unidadMedidaRepository.findByNombre(nombre);
    }
    
    /**
     * Obtiene todas las unidades de medida base
     * @return Lista de unidades de medida base
     */
    public List<UnidadMedida> getUnidadesMedidaBase() {
        return unidadMedidaRepository.findByEsUnidadBaseTrue();
    }
    
    /**
     * Guarda una unidad de medida
     * @param unidadMedida Unidad de medida a guardar
     * @return Unidad de medida guardada
     */
    public UnidadMedida saveUnidadMedida(UnidadMedida unidadMedida) {
        return unidadMedidaRepository.save(unidadMedida);
    }
    
    /**
     * Elimina una unidad de medida
     * @param id ID de la unidad de medida a eliminar
     */
    public void deleteUnidadMedida(Integer id) {
        unidadMedidaRepository.deleteById(id);
    }
    
    /**
     * Verifica si una unidad de medida existe por su nombre
     * @param nombre Nombre de la unidad de medida
     * @return true si existe, false en caso contrario
     */
    public boolean existsByNombre(String nombre) {
        return unidadMedidaRepository.findByNombre(nombre).isPresent();
    }
    
    /**
     * Verifica si una unidad de medida existe por su abreviatura
     * @param abreviatura Abreviatura de la unidad de medida
     * @return true si existe, false en caso contrario
     */
    public boolean existsByAbreviatura(String abreviatura) {
        return unidadMedidaRepository.findByAbreviatura(abreviatura).isPresent();
    }
}
