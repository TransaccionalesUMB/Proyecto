package com.example.transactional.repository;

import com.example.transactional.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    
    List<Stock> findByProductId(Integer productId);
    
    @Query("SELECT SUM(s.quantity) FROM Stock s WHERE s.productId = :productId")
    Integer getTotalStockByProductId(@Param("productId") Integer productId);
}
