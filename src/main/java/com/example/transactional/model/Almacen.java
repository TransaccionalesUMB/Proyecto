package com.example.transactional.model;

import javax.persistence.*;
import java.util.List;

/**
 * Modelo para representar almacenes
 * Un almacén es la entidad principal que contiene bodegas y sucursales
 */
@Entity
@Table(name = "almacen")
public class Almacen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_almacen")
    private Integer id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "capacidad")
    private Double capacidad;
    
    @OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL)
    private List<Sucursal> sucursales;
    
    @OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL)
    private List<Bodega> bodegas;
    
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
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Double getCapacidad() {
        return capacidad;
    }
    
    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }
    
    public List<Sucursal> getSucursales() {
        return sucursales;
    }
    
    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }
    
    public List<Bodega> getBodegas() {
        return bodegas;
    }
    
    public void setBodegas(List<Bodega> bodegas) {
        this.bodegas = bodegas;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
