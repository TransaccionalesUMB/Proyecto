package com.example.transactional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.transactional.model.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, String> {
    // Métodos personalizados si son necesarios
}
