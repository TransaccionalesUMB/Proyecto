package com.example.transactional.model;

import javax.persistence.*;

@Entity
@Table(name = "rol")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer id;
    
    @Column(name = "nombre")
    private String name;
    
    // Campos adicionales que no existen en la base de datos
    @Transient
    private String description;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
