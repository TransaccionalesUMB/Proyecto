package com.example.transactional.repository;

import com.example.transactional.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Mantiene Integer como tipo de ID ya que el Product sigue usando Integer como tipo de ID primaria
    // Los campos categoryId y providerId son los que cambiaron a String
}
