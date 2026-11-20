package com.example.device.repository;

import com.example.device.entity.PurchaseTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseTotalRepository extends JpaRepository<PurchaseTotal, Long> {
    List<PurchaseTotal> findByStatus(String status);
}
