package com.example.device.repository;

import com.example.device.entity.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
    List<RepairOrder> findByApplicantId(Long applicantId);
    List<RepairOrder> findByDeviceId(Long deviceId);
    List<RepairOrder> findByStatus(String status);
}
