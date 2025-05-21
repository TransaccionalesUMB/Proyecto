package com.example.transactional.model;

import javax.persistence.*;

/**
 * Modelo para representar el stock de productos
 * Incluye relaciones con bodegas, ubicaciones específicas y lotes
 */
@Entity
@Table(name = "stock")
public class Stock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Integer id;
    
    @Column(name = "id_producto")
    private Integer idProducto;
    
    @Column(name = "nombre_producto", length = 100)
    private String nombreProducto;
    
    @Column(name = "id_bodega")
    private Integer idBodega;
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private Product producto;
    
    @ManyToOne
    @JoinColumn(name = "id_bodega", insertable = false, updatable = false)
    private Bodega bodega;
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public Integer getIdBodega() {
        return idBodega;
    }
    
    public void setIdBodega(Integer idBodega) {
        this.idBodega = idBodega;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public Product getProducto() {
        return producto;
    }
    
    public void setProducto(Product producto) {
        this.producto = producto;
    }
    
    public Bodega getBodega() {
        return bodega;
    }
    
    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }
    
    /**
     * Verifica si el stock está próximo a caducar
     * @return false ya que no hay información de lote disponible en este modelo
     */
    public boolean isProximoACaducar() {
        return false; // No hay información de lote disponible
    }
    
    /**
     * Obtiene la ubicación completa del stock
     * @return Cadena con la jerarquía completa de ubicación
     */
    public String getUbicacionCompleta() {
        StringBuilder ubicacionCompleta = new StringBuilder();
        
        if (bodega != null) {
            if (bodega.getSucursal() != null && bodega.getSucursal().getAlmacen() != null) {
                ubicacionCompleta.append(bodega.getSucursal().getAlmacen().getNombre()).append(" > ");
            }
            if (bodega.getSucursal() != null) {
                ubicacionCompleta.append(bodega.getSucursal().getNombre()).append(" > ");
            }
            ubicacionCompleta.append(bodega.getNombre());
        }
        
        return ubicacionCompleta.toString();
    }
}
