package com.example.transactional.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Modelo para representar lotes de productos
 * Permite gestionar fechas de caducidad, trazabilidad y control de stock por lote
 */
@Entity
@Table(name = "lote")
public class Lote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lote")
    private Integer id;
    
    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;
    
    @Column(name = "numero_lote", nullable = false)
    private String numeroLote;
    
    @Column(name = "fecha_fabricacion")
    @Temporal(TemporalType.DATE)
    private Date fechaFabricacion;
    
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    
    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;
    
    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual;
    
    @Column(name = "id_proveedor")
    private String idProveedor;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private Product producto;
    
    @ManyToOne
    @JoinColumn(name = "id_proveedor", insertable = false, updatable = false)
    private Provider proveedor;
    
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
    
    public String getNumeroLote() {
        return numeroLote;
    }
    
    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }
    
    public Date getFechaFabricacion() {
        return fechaFabricacion;
    }
    
    public void setFechaFabricacion(Date fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }
    
    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }
    
    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
    
    public Integer getCantidadInicial() {
        return cantidadInicial;
    }
    
    public void setCantidadInicial(Integer cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }
    
    public Integer getCantidadActual() {
        return cantidadActual;
    }
    
    public void setCantidadActual(Integer cantidadActual) {
        this.cantidadActual = cantidadActual;
    }
    
    public String getIdProveedor() {
        return idProveedor;
    }
    
    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    public Product getProducto() {
        return producto;
    }
    
    public void setProducto(Product producto) {
        this.producto = producto;
    }
    
    public Provider getProveedor() {
        return proveedor;
    }
    
    public void setProveedor(Provider proveedor) {
        this.proveedor = proveedor;
    }
    
    /**
     * Verifica si el lote está próximo a caducar (menos de 30 días)
     * @return true si está próximo a caducar
     */
    public boolean isProximoACaducar() {
        if (fechaCaducidad == null) {
            return false;
        }
        
        long hoy = new Date().getTime();
        long caducidad = fechaCaducidad.getTime();
        long diferenciaDias = (caducidad - hoy) / (1000 * 60 * 60 * 24);
        
        return diferenciaDias <= 30 && diferenciaDias >= 0;
    }
    
    /**
     * Verifica si el lote ha caducado
     * @return true si ha caducado
     */
    public boolean isCaducado() {
        if (fechaCaducidad == null) {
            return false;
        }
        
        return fechaCaducidad.before(new Date());
    }
}
