package com.example.transactional.service;

import com.example.transactional.model.Almacen;
import com.example.transactional.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar los almacenes
 */
@Service
public class AlmacenService {
    
    @Autowired
    private AlmacenRepository almacenRepository;
    
    /**
     * Obtiene todos los almacenes
     * @return Lista de almacenes
     */
    public List<Almacen> getAllAlmacenes() {
        return almacenRepository.findAll();
    }
    
    /**
     * Obtiene todos los almacenes ordenados por nombre
     * @return Lista de almacenes ordenados
     */
    public List<Almacen> getAllAlmacenesOrdenados() {
        return almacenRepository.findAllByOrderByNombreAsc();
    }
    
    /**
     * Obtiene un almacén por su ID
     * @param id ID del almacén
     * @return Almacén encontrado o vacío
     */
    public Optional<Almacen> getAlmacenById(Integer id) {
        return almacenRepository.findById(id);
    }
    
    /**
     * Obtiene un almacén por su nombre
     * @param nombre Nombre del almacén
     * @return Almacén encontrado o vacío
     */
    public Optional<Almacen> getAlmacenByNombre(String nombre) {
        return almacenRepository.findByNombre(nombre);
    }
    
    /**
     * Obtiene almacenes con sus sucursales
     * @return Lista de almacenes con datos de sucursales
     */
    public List<Almacen> getAlmacenesWithSucursales() {
        return almacenRepository.findAllWithSucursales();
    }
    
    /**
     * Guarda un almacén nuevo
     * @param almacen Almacén a guardar
     * @return Almacén guardado
     */
    public Almacen saveAlmacen(Almacen almacen) {
        return almacenRepository.save(almacen);
    }
    
    /**
     * Actualiza un almacén existente
     * @param almacen Almacén con los datos actualizados
     * @return Almacén actualizado
     */
    public Almacen updateAlmacen(Almacen almacen) {
        // Verificar que el almacén existe
        if (!almacenRepository.existsById(almacen.getId())) {
            throw new IllegalArgumentException("El almacén con ID " + almacen.getId() + " no existe");
        }
        
        return almacenRepository.save(almacen);
    }
    
    /**
     * Elimina un almacén
     * @param id ID del almacén a eliminar
     */
    public void deleteAlmacen(Integer id) {
        almacenRepository.deleteById(id);
    }
    
    /**
     * Verifica si un almacén existe por su nombre
     * @param nombre Nombre del almacén
     * @return true si existe, false en caso contrario
     */
    public boolean existsByNombre(String nombre) {
        return almacenRepository.findByNombre(nombre).isPresent();
    }
    
    /**
     * Verifica si un almacén existe por su ID
     * @param id ID del almacén
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Integer id) {
        return almacenRepository.existsById(id);
    }
    
    /**
     * Asegura que un almacén existe, creándolo si es necesario
     * @param id ID del almacén
     * @param nombre Nombre por defecto si se crea
     * @return Almacén existente o creado
     */
    public Almacen ensureAlmacenExists(Integer id, String nombre) {
        Optional<Almacen> almacenOpt = almacenRepository.findById(id);
        
        if (almacenOpt.isPresent()) {
            return almacenOpt.get();
        } else {
            Almacen nuevoAlmacen = new Almacen();
            nuevoAlmacen.setId(id);
            nuevoAlmacen.setNombre(nombre);
            return almacenRepository.save(nuevoAlmacen);
        }
    }
}
