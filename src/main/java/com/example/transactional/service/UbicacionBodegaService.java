package com.example.transactional.service;

import com.example.transactional.model.UbicacionBodega;
import com.example.transactional.model.UbicacionBodega.TipoUbicacion;
import com.example.transactional.repository.UbicacionBodegaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las ubicaciones dentro de las bodegas
 */
@Service
public class UbicacionBodegaService {
    
    @Autowired
    private UbicacionBodegaRepository ubicacionBodegaRepository;
    
    @Autowired
    private BodegaService bodegaService;
    
    /**
     * Obtiene todas las ubicaciones
     * @return Lista de ubicaciones
     */
    public List<UbicacionBodega> getAllUbicaciones() {
        return ubicacionBodegaRepository.findAll();
    }
    
    /**
     * Obtiene una ubicación por su ID
     * @param id ID de la ubicación
     * @return Ubicación encontrada o vacío
     */
    public Optional<UbicacionBodega> getUbicacionById(Integer id) {
        return ubicacionBodegaRepository.findById(id);
    }
    
    /**
     * Obtiene ubicaciones por bodega
     * @param idBodega ID de la bodega
     * @return Lista de ubicaciones de la bodega
     */
    public List<UbicacionBodega> getUbicacionesByBodega(Integer idBodega) {
        return ubicacionBodegaRepository.findByIdBodega(idBodega);
    }
    
    /**
     * Obtiene ubicaciones activas por bodega
     * @param idBodega ID de la bodega
     * @return Lista de ubicaciones activas de la bodega
     */
    public List<UbicacionBodega> getUbicacionesActivasByBodega(Integer idBodega) {
        return ubicacionBodegaRepository.findByIdBodegaAndActivaTrue(idBodega);
    }
    
    /**
     * Obtiene ubicaciones por tipo
     * @param tipo Tipo de ubicación
     * @return Lista de ubicaciones de ese tipo
     */
    public List<UbicacionBodega> getUbicacionesByTipo(TipoUbicacion tipo) {
        return ubicacionBodegaRepository.findByTipo(tipo);
    }
    
    /**
     * Obtiene ubicaciones por bodega y tipo
     * @param idBodega ID de la bodega
     * @param tipo Tipo de ubicación
     * @return Lista de ubicaciones de la bodega y tipo
     */
    public List<UbicacionBodega> getUbicacionesByBodegaAndTipo(Integer idBodega, TipoUbicacion tipo) {
        return ubicacionBodegaRepository.findByIdBodegaAndTipo(idBodega, tipo);
    }
    
    /**
     * Obtiene una ubicación específica en una bodega
     * @param idBodega ID de la bodega
     * @param codigo Código de la ubicación
     * @return Ubicación encontrada o vacío
     */
    public Optional<UbicacionBodega> getUbicacionByBodegaAndCodigo(Integer idBodega, String codigo) {
        return ubicacionBodegaRepository.findByIdBodegaAndCodigo(idBodega, codigo);
    }
    
    /**
     * Obtiene ubicaciones disponibles para almacenamiento en una bodega
     * @param idBodega ID de la bodega
     * @return Lista de ubicaciones disponibles para almacenamiento
     */
    public List<UbicacionBodega> getUbicacionesDisponiblesParaAlmacenamiento(Integer idBodega) {
        return ubicacionBodegaRepository.findUbicacionesDisponiblesParaAlmacenamiento(idBodega);
    }
    
    /**
     * Guarda una ubicación
     * @param ubicacion Ubicación a guardar
     * @return Ubicación guardada
     */
    public UbicacionBodega saveUbicacion(UbicacionBodega ubicacion) {
        // Verificar que la bodega existe
        if (ubicacion.getIdBodega() != null && !bodegaService.existsById(ubicacion.getIdBodega())) {
            throw new IllegalArgumentException("La bodega con ID " + ubicacion.getIdBodega() + " no existe");
        }
        
        // Verificar que no exista otra ubicación con el mismo código en la misma bodega
        if (ubicacion.getId() == null) {
            Optional<UbicacionBodega> existente = ubicacionBodegaRepository.findByIdBodegaAndCodigo(
                ubicacion.getIdBodega(), ubicacion.getCodigo());
            
            if (existente.isPresent()) {
                throw new IllegalArgumentException("Ya existe una ubicación con el código " + 
                    ubicacion.getCodigo() + " en la bodega con ID " + ubicacion.getIdBodega());
            }
        }
        
        return ubicacionBodegaRepository.save(ubicacion);
    }
    
    /**
     * Elimina una ubicación
     * @param id ID de la ubicación a eliminar
     */
    public void deleteUbicacion(Integer id) {
        ubicacionBodegaRepository.deleteById(id);
    }
    
    /**
     * Activa o desactiva una ubicación
     * @param id ID de la ubicación
     * @param activa true para activar, false para desactivar
     * @return Ubicación actualizada o vacío si no existe
     */
    public Optional<UbicacionBodega> activarDesactivarUbicacion(Integer id, boolean activa) {
        Optional<UbicacionBodega> ubicacionOpt = ubicacionBodegaRepository.findById(id);
        
        if (ubicacionOpt.isPresent()) {
            UbicacionBodega ubicacion = ubicacionOpt.get();
            ubicacion.setActiva(activa);
            return Optional.of(ubicacionBodegaRepository.save(ubicacion));
        }
        
        return Optional.empty();
    }
    
    /**
     * Verifica si una ubicación existe por su ID
     * @param id ID de la ubicación
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Integer id) {
        return ubicacionBodegaRepository.existsById(id);
    }
    
    /**
     * Asegura que una ubicación existe, creándola si es necesario
     * @param idBodega ID de la bodega
     * @param codigo Código de la ubicación
     * @return Ubicación existente o creada
     */
    public UbicacionBodega ensureUbicacionExists(Integer idBodega, String codigo) {
        Optional<UbicacionBodega> ubicacionOpt = ubicacionBodegaRepository.findByIdBodegaAndCodigo(
            idBodega, codigo);
        
        if (ubicacionOpt.isPresent()) {
            return ubicacionOpt.get();
        } else {
            // Asegurar que la bodega existe
            bodegaService.ensureBodegaExists(idBodega, "Bodega " + idBodega, 1);
            
            UbicacionBodega nuevaUbicacion = new UbicacionBodega();
            nuevaUbicacion.setIdBodega(idBodega);
            nuevaUbicacion.setCodigo(codigo);
            nuevaUbicacion.setTipo(TipoUbicacion.Almacenamiento);
            nuevaUbicacion.setActiva(true);
            return ubicacionBodegaRepository.save(nuevaUbicacion);
        }
    }
    
    /**
     * Actualiza una ubicación existente
     * @param ubicacion Ubicación con los datos actualizados
     * @return Ubicación actualizada
     */
    public UbicacionBodega updateUbicacion(UbicacionBodega ubicacion) {
        // Verificar que la ubicación existe
        if (ubicacion.getId() == null || !existsById(ubicacion.getId())) {
            throw new IllegalArgumentException("La ubicación a actualizar no existe");
        }
        
        // Verificar que la bodega existe
        if (ubicacion.getIdBodega() != null && !bodegaService.existsById(ubicacion.getIdBodega())) {
            throw new IllegalArgumentException("La bodega con ID " + ubicacion.getIdBodega() + " no existe");
        }
        
        // Verificar que no exista otra ubicación con el mismo código en la misma bodega (que no sea esta misma)
        Optional<UbicacionBodega> existente = ubicacionBodegaRepository.findByIdBodegaAndCodigo(
            ubicacion.getIdBodega(), ubicacion.getCodigo());
        
        if (existente.isPresent() && !existente.get().getId().equals(ubicacion.getId())) {
            throw new IllegalArgumentException("Ya existe otra ubicación con el código " + 
                ubicacion.getCodigo() + " en la bodega con ID " + ubicacion.getIdBodega());
        }
        
        return ubicacionBodegaRepository.save(ubicacion);
    }
}
