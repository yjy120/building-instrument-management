package com.example.device.repository;

import com.example.device.entity.PurchaseApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseApplyRepository extends JpaRepository<PurchaseApply, Long> {
    List<PurchaseApply> findByApplicantId(Long applicantId);
    List<PurchaseApply> findByStatus(String status);
    List<PurchaseApply> findByStatusIn(List<String> statuses);
}
