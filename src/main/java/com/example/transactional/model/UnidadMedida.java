package com.example.transactional.model;

import javax.persistence.*;

/**
 * Modelo para representar unidades de medida para productos
 * Permite gestionar diferentes unidades como unidad, caja, kilogramo, litro, etc.
 */
@Entity
@Table(name = "unidad_medida")
public class UnidadMedida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidad")
    private Integer id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "abreviatura", nullable = false)
    private String abreviatura;
    
    @Column(name = "es_unidad_base")
    private Boolean esUnidadBase;
    
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
    
    public String getAbreviatura() {
        return abreviatura;
    }
    
    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
    
    public Boolean getEsUnidadBase() {
        return esUnidadBase;
    }
    
    public void setEsUnidadBase(Boolean esUnidadBase) {
        this.esUnidadBase = esUnidadBase;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + abreviatura + ")";
    }
}
