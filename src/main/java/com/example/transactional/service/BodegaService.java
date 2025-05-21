package com.example.transactional.service;

import com.example.transactional.model.Bodega;
import com.example.transactional.repository.BodegaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las bodegas
 */
@Service
public class BodegaService {
    
    @Autowired
    private BodegaRepository bodegaRepository;
    
    @Autowired
    private SucursalService sucursalService;
    
    /**
     * Obtiene todas las bodegas
     * @return Lista de bodegas
     */
    public List<Bodega> getAllBodegas() {
        return bodegaRepository.findAll();
    }
    
    /**
     * Obtiene todas las bodegas ordenadas por nombre
     * @return Lista de bodegas ordenadas
     */
    public List<Bodega> getAllBodegasOrdenadas() {
        return bodegaRepository.findAllByOrderByNombreAsc();
    }
    
    /**
     * Obtiene una bodega por su ID
     * @param id ID de la bodega
     * @return Bodega encontrada o vacío
     */
    public Optional<Bodega> getBodegaById(Integer id) {
        return bodegaRepository.findById(id);
    }
    
    /**
     * Obtiene una bodega por su nombre
     * @param nombre Nombre de la bodega
     * @return Bodega encontrada o vacío
     */
    public Optional<Bodega> getBodegaByNombre(String nombre) {
        return bodegaRepository.findByNombre(nombre);
    }
    
    /**
     * Obtiene bodegas por sucursal
     * @param idSucursal ID de la sucursal
     * @return Lista de bodegas de la sucursal
     */
    public List<Bodega> getBodegasBySucursal(Integer idSucursal) {
        return bodegaRepository.findByIdSucursal(idSucursal);
    }
    
    /**
     * Obtiene bodegas por almacén
     * @param idAlmacen ID del almacén
     * @return Lista de bodegas del almacén
     */
    public List<Bodega> getBodegasByAlmacen(Integer idAlmacen) {
        return bodegaRepository.findByIdAlmacen(idAlmacen);
    }
    
    /**
     * Obtiene bodegas con sus ubicaciones
     * @return Lista de bodegas con datos de ubicaciones
     */
    public List<Bodega> getBodegasWithUbicaciones() {
        return bodegaRepository.findAllWithUbicaciones();
    }
    
    /**
     * Obtiene bodegas con sus ubicaciones por sucursal
     * @param idSucursal ID de la sucursal
     * @return Lista de bodegas con datos de ubicaciones de la sucursal
     */
    public List<Bodega> getBodegasWithUbicacionesBySucursal(Integer idSucursal) {
        return bodegaRepository.findByIdSucursalWithUbicaciones(idSucursal);
    }
    
    /**
     * Obtiene bodegas con stock disponible para un producto
     * @param idProducto ID del producto
     * @return Lista de bodegas con stock disponible del producto
     */
    public List<Bodega> getBodegasWithStockByProducto(Integer idProducto) {
        return bodegaRepository.findBodegasWithStockByProducto(idProducto);
    }
    
    /**
     * Guarda una bodega
     * @param bodega Bodega a guardar
     * @return Bodega guardada
     */
    public Bodega saveBodega(Bodega bodega) {
        // Verificar que la sucursal existe si se proporciona
        if (bodega.getIdSucursal() != null && !sucursalService.existsById(bodega.getIdSucursal())) {
            throw new IllegalArgumentException("La sucursal con ID " + bodega.getIdSucursal() + " no existe");
        }
        
        return bodegaRepository.save(bodega);
    }
    
    /**
     * Actualiza una bodega existente
     * @param bodega Bodega con los datos actualizados
     * @return Bodega actualizada
     */
    public Bodega updateBodega(Bodega bodega) {
        // Verificar que la bodega existe
        if (!bodegaRepository.existsById(bodega.getId())) {
            throw new IllegalArgumentException("La bodega con ID " + bodega.getId() + " no existe");
        }
        
        // Verificar que la sucursal existe si se proporciona
        if (bodega.getIdSucursal() != null && !sucursalService.existsById(bodega.getIdSucursal())) {
            throw new IllegalArgumentException("La sucursal con ID " + bodega.getIdSucursal() + " no existe");
        }
        
        return bodegaRepository.save(bodega);
    }
    
    /**
     * Elimina una bodega
     * @param id ID de la bodega a eliminar
     */
    public void deleteBodega(Integer id) {
        bodegaRepository.deleteById(id);
    }
    
    /**
     * Verifica si una bodega existe por su nombre
     * @param nombre Nombre de la bodega
     * @return true si existe, false en caso contrario
     */
    public boolean existsByNombre(String nombre) {
        return bodegaRepository.findByNombre(nombre).isPresent();
    }
    
    /**
     * Verifica si una bodega existe por su ID
     * @param id ID de la bodega
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Integer id) {
        return bodegaRepository.existsById(id);
    }
    
    /**
     * Asegura que una bodega existe, creándola si es necesario
     * @param id ID de la bodega
     * @param nombre Nombre por defecto si se crea
     * @param idSucursal ID de la sucursal a la que pertenece
     * @return Bodega existente o creada
     */
    public Bodega ensureBodegaExists(Integer id, String nombre, Integer idSucursal) {
        Optional<Bodega> bodegaOpt = bodegaRepository.findById(id);
        
        if (bodegaOpt.isPresent()) {
            return bodegaOpt.get();
        } else {
            // Asegurar que la sucursal existe
            sucursalService.ensureSucursalExists(idSucursal, "Sucursal " + idSucursal, 1);
            
            Bodega nuevaBodega = new Bodega();
            nuevaBodega.setId(id);
            nuevaBodega.setNombre(nombre);
            nuevaBodega.setIdSucursal(idSucursal);
            return bodegaRepository.save(nuevaBodega);
        }
    }
}
