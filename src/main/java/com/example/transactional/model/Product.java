package com.example.transactional.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Producto")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;
    
    @Column(name = "nombre", nullable = false)
    private String name;
    
    @Column(name = "descripcion")
    private String description;
    
    @Column(name = "precio", nullable = false)
    private BigDecimal price;
    
    @Column(name = "id_categoria")
    private Integer categoryId;
    
    @Column(name = "id_proveedor")
    private Integer providerId;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }
}
