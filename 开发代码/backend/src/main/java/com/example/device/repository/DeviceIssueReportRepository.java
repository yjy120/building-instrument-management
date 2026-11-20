package com.example.device.repository;

import com.example.device.entity.DeviceIssueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceIssueReportRepository extends JpaRepository<DeviceIssueReport, Long> {
    
    List<DeviceIssueReport> findByStatus(String status);
    
    List<DeviceIssueReport> findByApplicantId(Long applicantId);
    
    List<DeviceIssueReport> findByDeviceId(Long deviceId);
    
    List<DeviceIssueReport> findByDeviceType(String deviceType);
    
    List<DeviceIssueReport> findByStatusOrderByCreateTimeDesc(String status);
}
