package com.example.device.repository;

import com.example.device.entity.UseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UseRecordRepository extends JpaRepository<UseRecord, Long> {
    List<UseRecord> findByUserId(Long userId);
    List<UseRecord> findByDeviceId(Long deviceId);
    List<UseRecord> findByStatus(String status);
}
