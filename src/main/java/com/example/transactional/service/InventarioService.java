package com.example.transactional.service;

import com.example.transactional.model.*;
import com.example.transactional.repository.LoteRepository;
import com.example.transactional.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión avanzada de inventario
 * Integra las funcionalidades de stock, lotes, ubicaciones y jerarquía de almacenamiento
 */
@Service
public class InventarioService {
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private LoteService loteService;
    
    @Autowired
    private BodegaService bodegaService;
    
    @Autowired
    private UbicacionBodegaService ubicacionService;
    
    @Autowired
    private LoteRepository loteRepository;
    
    /**
     * Obtiene todo el stock disponible
     * @return Lista de stock
     */
    public List<Stock> getAllStock() {
        try {
            return stockRepository.findAll();
        } catch (Exception e) {
            // Registrar el error y devolver una lista vacía
            System.err.println("Error al obtener stock: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene stock por producto
     * @param idProducto ID del producto
     * @return Lista de stock del producto
     */
    public List<Stock> getStockByProducto(Integer idProducto) {
        try {
            return stockRepository.findByIdProducto(idProducto);
        } catch (Exception e) {
            System.err.println("Error al obtener stock por producto: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene stock por bodega
     * @param idBodega ID de la bodega
     * @return Lista de stock de la bodega
     */
    public List<Stock> getStockByBodega(Integer idBodega) {
        try {
            return stockRepository.findByIdBodega(idBodega);
        } catch (Exception e) {
            System.err.println("Error al obtener stock por bodega: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene stock por ubicación
     * @param idUbicacion ID de la ubicación
     * @return Lista vacía ya que el modelo Stock ya no tiene relación con ubicaciones
     * @deprecated El modelo Stock ya no tiene relación con ubicaciones
     */
    @Deprecated
    public List<Stock> getStockByUbicacion(Integer idUbicacion) {
        System.err.println("Método obsoleto: getStockByUbicacion. El modelo Stock ya no tiene relación con ubicaciones.");
        return new ArrayList<>();
    }
    
    /**
     * Obtiene stock por lote
     * @param idLote ID del lote
     * @return Lista vacía ya que el modelo Stock ya no tiene relación con lotes
     * @deprecated El modelo Stock ya no tiene relación con lotes
     */
    @Deprecated
    public List<Stock> getStockByLote(Integer idLote) {
        System.err.println("Método obsoleto: getStockByLote. El modelo Stock ya no tiene relación con lotes.");
        return new ArrayList<>();
    }
    
    /**
     * Obtiene stock por producto y bodega
     * @param idProducto ID del producto
     * @param idBodega ID de la bodega
     * @return Lista de stock del producto en la bodega
     */
    public List<Stock> getStockByProductoAndBodega(Integer idProducto, Integer idBodega) {
        try {
            return stockRepository.findByIdProductoAndIdBodega(idProducto, idBodega);
        } catch (Exception e) {
            System.err.println("Error al obtener stock por producto y bodega: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene stock por producto y ubicación
     * @param idProducto ID del producto
     * @param idUbicacion ID de la ubicación
     * @return Lista de stock del producto en la ubicación
     */
    /**
     * Obtiene stock por producto y ubicación
     * @param idProducto ID del producto
     * @param idUbicacion ID de la ubicación
     * @return Lista vacía ya que el modelo Stock ya no tiene relación con ubicaciones
     * @deprecated El modelo Stock ya no tiene relación con ubicaciones
     */
    @Deprecated
    public List<Stock> getStockByProductoAndUbicacion(Integer idProducto, Integer idUbicacion) {
        System.err.println("Método obsoleto: getStockByProductoAndUbicacion. El modelo Stock ya no tiene relación con ubicaciones.");
        return new ArrayList<>();
    }
    
    /**
     * Obtiene la cantidad total de stock de un producto
     * @param idProducto ID del producto
     * @return Cantidad total de stock
     */
    public Integer getTotalStockByProducto(Integer idProducto) {
        Integer total = stockRepository.getTotalStockByProductId(idProducto);
        return total != null ? total : 0;
    }
    
    /**
     * Obtiene la cantidad total de stock de un producto en una bodega
     * @param idProducto ID del producto
     * @param idBodega ID de la bodega
     * @return Cantidad total de stock
     */
    public Integer getTotalStockByProductoAndBodega(Integer idProducto, Integer idBodega) {
        Integer total = stockRepository.getTotalStockByProductIdAndWarehouseId(idProducto, idBodega);
        return total != null ? total : 0;
    }
    
    /**
     * Registra entrada de stock
     * @param idProducto ID del producto
     * @param idBodega ID de la bodega
     * @param idUbicacion ID de la ubicación (opcional)
     * @param cantidad Cantidad a ingresar
     * @param numeroLote Número de lote (opcional)
     * @param fechaCaducidad Fecha de caducidad (opcional)
     * @return Stock creado o actualizado
     */
    @Transactional
    public Stock registrarEntrada(Integer idProducto, Integer idBodega, Integer idUbicacion, 
                                  Integer cantidad, String numeroLote, Date fechaCaducidad) {
        
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        
        // Verificar que el producto existe
        Product producto = productService.getProductEntityById(idProducto)
            .orElseThrow(() -> new IllegalArgumentException("El producto con ID " + idProducto + " no existe"));
        
        // Verificar que la bodega existe
        bodegaService.ensureBodegaExists(idBodega, "Bodega " + idBodega, 1);
        
        // Verificar que la ubicación existe si se proporciona
        Integer ubicacionId = idUbicacion;
        if (ubicacionId == null) {
            // Si no se proporciona ubicación, usar la primera ubicación de almacenamiento disponible
            List<UbicacionBodega> ubicaciones = ubicacionService.getUbicacionesDisponiblesParaAlmacenamiento(idBodega);
            if (!ubicaciones.isEmpty()) {
                ubicacionId = ubicaciones.get(0).getId();
            } else {
                // Si no hay ubicaciones, crear una por defecto
                UbicacionBodega ubicacion = ubicacionService.ensureUbicacionExists(idBodega, "A-01");
                ubicacionId = ubicacion.getId();
            }
        }
        
        // Gestionar el lote
        Integer loteId = null;
        if (numeroLote != null) {
            // Buscar si el lote ya existe
            List<Lote> lotes = loteService.getLotesByNumeroLote(numeroLote);
            Lote lote = null;
            
            if (!lotes.isEmpty()) {
                // Usar el primer lote encontrado con ese número
                lote = lotes.get(0);
                // Actualizar la cantidad actual del lote
                lote.setCantidadActual(lote.getCantidadActual() + cantidad);
                lote = loteService.saveLote(lote);
            } else {
                // Crear un nuevo lote
                lote = new Lote();
                lote.setIdProducto(idProducto);
                lote.setNumeroLote(numeroLote);
                lote.setFechaFabricacion(new Date());
                lote.setFechaCaducidad(fechaCaducidad);
                lote.setCantidadInicial(cantidad);
                lote.setCantidadActual(cantidad);
                lote.setIdProveedor(producto.getProviderId());
                lote = loteService.saveLote(lote);
            }
            
            loteId = lote.getId();
        }
        
        // Crear o actualizar el stock
        List<Stock> stocks = stockRepository.findByIdProductoAndIdBodega(idProducto, idBodega);
        Stock stock;
        
        if (stocks.isEmpty()) {
            // Crear nuevo registro de stock
            stock = new Stock();
            stock.setIdProducto(idProducto);
            stock.setNombreProducto(producto.getName());
            stock.setIdBodega(idBodega);
            stock.setCantidad(cantidad);
        } else {
            // Actualizar stock existente
            stock = stocks.get(0);
            stock.setCantidad(stock.getCantidad() + cantidad);
        }
        
        return stockRepository.save(stock);
    }
    
    /**
     * Registra salida de stock
     * @param idProducto ID del producto
     * @param idBodega ID de la bodega
     * @param idUbicacion ID de la ubicación (opcional)
     * @param idLote ID del lote (opcional)
     * @param cantidad Cantidad a retirar
     * @return Stock actualizado
     */
    @Transactional
    public Stock registrarSalida(Integer idProducto, Integer idBodega, Integer idUbicacion, 
                                Integer idLote, Integer cantidad) {
        
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        
        // Buscar stock según los parámetros proporcionados
        List<Stock> stocks = stockRepository.findByIdProductoAndIdBodega(idProducto, idBodega);
        
        if (stocks.isEmpty()) {
            throw new IllegalArgumentException("No hay stock disponible para el producto con ID " + idProducto);
        }
        
        // Usar el primer stock disponible ya que no hay soporte para lotes en el modelo actual
        Stock stockSeleccionado = stocks.get(0);
        
        // Verificar que hay suficiente stock
        if (stockSeleccionado.getCantidad() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock disponible. Disponible: " + 
                stockSeleccionado.getCantidad() + ", Solicitado: " + cantidad);
        }
        
        // Actualizar el stock
        stockSeleccionado.setCantidad(stockSeleccionado.getCantidad() - cantidad);
        
        // Si el stock queda en cero, eliminarlo
        if (stockSeleccionado.getCantidad() == 0) {
            stockRepository.delete(stockSeleccionado);
            return stockSeleccionado; // Devolver el stock eliminado
        }
        
        // Ya no se actualiza el lote ya que el modelo Stock ya no tiene relación con lotes
        
        return stockRepository.save(stockSeleccionado);
    }
    
    /**
     * Transfiere stock entre ubicaciones
     * @param idProducto ID del producto
     * @param idBodegaOrigen ID de la bodega origen
     * @param idUbicacionOrigen ID de la ubicación origen (opcional)
     * @param idLote ID del lote (opcional)
     * @param idBodegaDestino ID de la bodega destino
     * @param idUbicacionDestino ID de la ubicación destino (opcional)
     * @param cantidad Cantidad a transferir
     * @return Stock en la ubicación destino
     */
    @Transactional
    public Stock transferirStock(Integer idProducto, Integer idBodegaOrigen, Integer idUbicacionOrigen, 
                               Integer idLote, Integer idBodegaDestino, Integer idUbicacionDestino, 
                               Integer cantidad) {
        
        // Registrar salida del origen
        registrarSalida(idProducto, idBodegaOrigen, idUbicacionOrigen, idLote, cantidad);
        
        // Registrar entrada en el destino
        // Si hay lote, obtener sus datos para mantener la trazabilidad
        String numeroLote = null;
        Date fechaCaducidad = null;
        
        if (idLote != null) {
            Optional<Lote> loteOpt = loteService.getLoteById(idLote);
            if (loteOpt.isPresent()) {
                Lote lote = loteOpt.get();
                numeroLote = lote.getNumeroLote();
                fechaCaducidad = lote.getFechaCaducidad();
            }
        }
        
        return registrarEntrada(idProducto, idBodegaDestino, idUbicacionDestino, cantidad, numeroLote, fechaCaducidad);
    }
    
    /**
     * Obtiene productos con stock bajo (menos del umbral especificado)
     * @param umbral Umbral mínimo de stock
     * @return Lista de productos con stock bajo
     */
    public List<Product> getProductosConStockBajo(Integer umbral) {
        // Esta implementación dependerá de cómo esté estructurada la consulta en el repositorio
        // Por ahora, dejamos un comentario indicando que se debe implementar
        throw new UnsupportedOperationException("Método no implementado aún");
    }
    
    /**
     * Obtiene productos próximos a caducar
     * @param diasLimite Días límite para considerar próximos a caducar
     * @return Lista de productos próximos a caducar
     */
    public List<Lote> getLotesProximosACaducar(Integer diasLimite) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, diasLimite);
        Date fechaLimite = calendar.getTime();
        
        return loteRepository.findByFechaCaducidadBefore(fechaLimite);
    }
}
