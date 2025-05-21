package com.example.transactional.model;

import javax.persistence.*;

@Entity
@Table(name = "Categoria")
public class Category {
    
    @Id
    @Column(name = "id_categoria")
    private String id;
    
    @Column(name = "nombre")
    private String name;
    
    // No hay columna descripcion en la tabla Categoria
    @Transient // Esta anotación indica que este campo no se mapea a ninguna columna
    private String description;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
