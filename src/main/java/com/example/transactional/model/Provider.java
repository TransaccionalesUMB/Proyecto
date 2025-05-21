package com.example.transactional.model;

import javax.persistence.*;

@Entity
@Table(name = "Proveedor")
public class Provider {
    
    @Id
    @Column(name = "id_proveedor")
    private String id;
    
    @Column(name = "nombre")
    private String name;
    
    @Column(name = "contacto")
    private String contact;
    
    // Los siguientes campos no existen en la tabla Proveedor
    @Transient
    private String phone;
    
    @Transient
    private String address;
    
    @Transient
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
