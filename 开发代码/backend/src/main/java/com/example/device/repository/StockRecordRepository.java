package com.example.device.repository;

import com.example.device.entity.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    List<StockRecord> findByDeviceId(Long deviceId);
    List<StockRecord> findByType(String type);
}
