package com.example.transactional.model;

import javax.persistence.*;
import java.util.List;

/**
 * Modelo para representar bodegas
 * Una bodega pertenece a un almacén y contiene ubicaciones específicas
 */
@Entity
@Table(name = "bodega")
public class Bodega {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bodega")
    private Integer id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "capacidad")
    private Double capacidad;
    
    @Column(name = "temperatura")
    private Double temperatura;
    
    @Column(name = "humedad")
    private Double humedad;
    
    @Column(name = "id_sucursal")
    private Integer idSucursal;
    
    @Column(name = "id_almacen")
    private Integer idAlmacen;
    
    @ManyToOne
    @JoinColumn(name = "id_sucursal", insertable = false, updatable = false)
    private Sucursal sucursal;
    
    @ManyToOne
    @JoinColumn(name = "id_almacen", insertable = false, updatable = false)
    private Almacen almacen;
    
    @OneToMany(mappedBy = "bodega", cascade = CascadeType.ALL)
    private List<UbicacionBodega> ubicaciones;
    
    @OneToMany(mappedBy = "bodega")
    private List<Stock> stocks;
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Integer getIdSucursal() {
        return idSucursal;
    }
    
    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }
    
    public Integer getIdAlmacen() {
        return idAlmacen;
    }
    
    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }
    
    public Sucursal getSucursal() {
        return sucursal;
    }
    
    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }
    
    public Almacen getAlmacen() {
        return almacen;
    }
    
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
        if (almacen != null) {
            this.idAlmacen = almacen.getId();
        }
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Double getCapacidad() {
        return capacidad;
    }
    
    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }
    
    public Double getTemperatura() {
        return temperatura;
    }
    
    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }
    
    public Double getHumedad() {
        return humedad;
    }
    
    public void setHumedad(Double humedad) {
        this.humedad = humedad;
    }
    
    public List<UbicacionBodega> getUbicaciones() {
        return ubicaciones;
    }
    
    public void setUbicaciones(List<UbicacionBodega> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }
    
    public List<Stock> getStocks() {
        return stocks;
    }
    
    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
    
    /**
     * Obtiene la ubicación completa de la bodega
     * @return Nombre de la sucursal + nombre de la bodega
     */
    public String getUbicacionCompleta() {
        if (sucursal != null) {
            return sucursal.getNombre() + " - " + nombre;
        }
        return nombre;
    }
    
    /**
     * Obtiene la jerarquía completa de la bodega
     * @return Nombre del almacén + nombre de la sucursal + nombre de la bodega
     */
    public String getJerarquiaCompleta() {
        if (sucursal != null && sucursal.getAlmacen() != null) {
            return sucursal.getAlmacen().getNombre() + " > " + sucursal.getNombre() + " > " + nombre;
        } else if (sucursal != null) {
            return sucursal.getNombre() + " > " + nombre;
        }
        return nombre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
