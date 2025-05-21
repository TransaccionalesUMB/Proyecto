package com.example.transactional.service;

import com.example.transactional.model.Sucursal;
import com.example.transactional.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las sucursales
 */
@Service
public class SucursalService {
    
    @Autowired
    private SucursalRepository sucursalRepository;
    
    @Autowired
    private AlmacenService almacenService;
    
    /**
     * Obtiene todas las sucursales
     * @return Lista de sucursales
     */
    public List<Sucursal> getAllSucursales() {
        return sucursalRepository.findAll();
    }
    
    /**
     * Obtiene todas las sucursales ordenadas por nombre
     * @return Lista de sucursales ordenadas
     */
    public List<Sucursal> getAllSucursalesOrdenadas() {
        return sucursalRepository.findAllByOrderByNombreAsc();
    }
    
    /**
     * Obtiene una sucursal por su ID
     * @param id ID de la sucursal
     * @return Sucursal encontrada o vacío
     */
    public Optional<Sucursal> getSucursalById(Integer id) {
        return sucursalRepository.findById(id);
    }
    
    /**
     * Obtiene una sucursal por su nombre
     * @param nombre Nombre de la sucursal
     * @return Sucursal encontrada o vacío
     */
    public Optional<Sucursal> getSucursalByNombre(String nombre) {
        return sucursalRepository.findByNombre(nombre);
    }
    
    /**
     * Obtiene sucursales por almacén
     * @param idAlmacen ID del almacén
     * @return Lista de sucursales del almacén
     */
    public List<Sucursal> getSucursalesByAlmacen(Integer idAlmacen) {
        return sucursalRepository.findByIdAlmacen(idAlmacen);
    }
    
    /**
     * Obtiene sucursales con sus bodegas
     * @return Lista de sucursales con datos de bodegas
     */
    public List<Sucursal> getSucursalesWithBodegas() {
        return sucursalRepository.findAllWithBodegas();
    }
    
    /**
     * Obtiene sucursales con sus bodegas por almacén
     * @param idAlmacen ID del almacén
     * @return Lista de sucursales con datos de bodegas del almacén
     */
    public List<Sucursal> getSucursalesWithBodegasByAlmacen(Integer idAlmacen) {
        return sucursalRepository.findByIdAlmacenWithBodegas(idAlmacen);
    }
    
    /**
     * Guarda una sucursal
     * @param sucursal Sucursal a guardar
     * @return Sucursal guardada
     */
    public Sucursal saveSucursal(Sucursal sucursal) {
        // Verificar que el almacén existe
        if (sucursal.getIdAlmacen() != null && !almacenService.existsById(sucursal.getIdAlmacen())) {
            throw new IllegalArgumentException("El almacén con ID " + sucursal.getIdAlmacen() + " no existe");
        }
        
        return sucursalRepository.save(sucursal);
    }
    
    /**
     * Elimina una sucursal
     * @param id ID de la sucursal a eliminar
     */
    public void deleteSucursal(Integer id) {
        sucursalRepository.deleteById(id);
    }
    
    /**
     * Verifica si una sucursal existe por su nombre
     * @param nombre Nombre de la sucursal
     * @return true si existe, false en caso contrario
     */
    public boolean existsByNombre(String nombre) {
        return sucursalRepository.findByNombre(nombre).isPresent();
    }
    
    /**
     * Verifica si una sucursal existe por su ID
     * @param id ID de la sucursal
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Integer id) {
        return sucursalRepository.existsById(id);
    }
    
    /**
     * Asegura que una sucursal existe, creándola si es necesario
     * @param id ID de la sucursal
     * @param nombre Nombre por defecto si se crea
     * @param idAlmacen ID del almacén al que pertenece
     * @return Sucursal existente o creada
     */
    public Sucursal ensureSucursalExists(Integer id, String nombre, Integer idAlmacen) {
        Optional<Sucursal> sucursalOpt = sucursalRepository.findById(id);
        
        if (sucursalOpt.isPresent()) {
            return sucursalOpt.get();
        } else {
            // Asegurar que el almacén existe
            almacenService.ensureAlmacenExists(idAlmacen, "Almacén " + idAlmacen);
            
            Sucursal nuevaSucursal = new Sucursal();
            nuevaSucursal.setId(id);
            nuevaSucursal.setNombre(nombre);
            nuevaSucursal.setIdAlmacen(idAlmacen);
            return sucursalRepository.save(nuevaSucursal);
        }
    }
}
