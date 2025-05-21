package com.example.transactional.model;

import javax.persistence.*;
import java.util.List;

/**
 * Modelo para representar sucursales
 * Una sucursal pertenece a un almacén y contiene bodegas
 */
@Entity
@Table(name = "sucursal")
public class Sucursal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Integer id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "id_almacen")
    private Integer idAlmacen;
    
    @ManyToOne
    @JoinColumn(name = "id_almacen", insertable = false, updatable = false)
    private Almacen almacen;
    
    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
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
    
    public Integer getIdAlmacen() {
        return idAlmacen;
    }
    
    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }
    
    public Almacen getAlmacen() {
        return almacen;
    }
    
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }
    
    public List<Bodega> getBodegas() {
        return bodegas;
    }
    
    public void setBodegas(List<Bodega> bodegas) {
        this.bodegas = bodegas;
    }
    
    /**
     * Obtiene la ubicación completa de la sucursal
     * @return Nombre del almacén + nombre de la sucursal
     */
    public String getUbicacionCompleta() {
        if (almacen != null) {
            return almacen.getNombre() + " - " + nombre;
        }
        return nombre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
