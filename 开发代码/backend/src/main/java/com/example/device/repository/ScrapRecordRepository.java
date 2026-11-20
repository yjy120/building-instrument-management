package com.example.device.repository;

import com.example.device.entity.ScrapRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRecordRepository extends JpaRepository<ScrapRecord, Long> {
    List<ScrapRecord> findByApplicantId(Long applicantId);
    List<ScrapRecord> findByStatus(String status);
}
