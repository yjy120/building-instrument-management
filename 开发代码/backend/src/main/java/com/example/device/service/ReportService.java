package com.example.device.service;

import com.example.device.entity.Device;
import com.example.device.entity.RepairOrder;
import com.example.device.entity.ScrapRecord;
import com.example.device.entity.UseRecord;
import com.example.device.repository.DeviceRepository;
import com.example.device.repository.RepairOrderRepository;
import com.example.device.repository.ScrapRecordRepository;
import com.example.device.repository.UseRecordRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    
    private final DeviceRepository deviceRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final ScrapRecordRepository scrapRecordRepository;
    private final UseRecordRepository useRecordRepository;
    
    public ReportService(DeviceRepository deviceRepository,
                        RepairOrderRepository repairOrderRepository,
                        ScrapRecordRepository scrapRecordRepository,
                        UseRecordRepository useRecordRepository) {
        this.deviceRepository = deviceRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.scrapRecordRepository = scrapRecordRepository;
        this.useRecordRepository = useRecordRepository;
    }
    
    public Map<String, Object> getDeviceStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Device> allDevices = deviceRepository.findAll();
        long totalCount = allDevices.size();
        
        long largeCount = allDevices.stream().filter(d -> "LARGE".equals(d.getDeviceType())).count();
        long smallCount = allDevices.stream().filter(d -> "SMALL".equals(d.getDeviceType())).count();
        
        long inUseCount = allDevices.stream().filter(d -> "IN_USE".equals(d.getStatus())).count();
        long repairingCount = allDevices.stream().filter(d -> "REPAIRING".equals(d.getStatus())).count();
        long borrowedCount = allDevices.stream().filter(d -> "BORROWED".equals(d.getStatus())).count();
        long idleCount = allDevices.stream().filter(d -> "IDLE".equals(d.getStatus())).count();
        
        stats.put("totalCount", totalCount);
        stats.put("largeCount", largeCount);
        stats.put("smallCount", smallCount);
        stats.put("inUseCount", inUseCount);
        stats.put("repairingCount", repairingCount);
        stats.put("borrowedCount", borrowedCount);
        stats.put("idleCount", idleCount);
        
        Map<String, Long> categoryStats = new HashMap<>();
        allDevices.forEach(d -> {
            String category = d.getCategory() != null ? d.getCategory() : "未分类";
            categoryStats.merge(category, 1L, Long::sum);
        });
        stats.put("categoryStats", categoryStats);
        
        return stats;
    }
    
    public Map<String, Object> getRepairStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<RepairOrder> allRepairs = repairOrderRepository.findAll();
        long totalRepairCount = allRepairs.size();
        
        long pendingRepairCount = allRepairs.stream().filter(r -> "PENDING".equals(r.getStatus())).count();
        long repairingRepairCount = allRepairs.stream().filter(r -> "REPAIRING".equals(r.getStatus())).count();
        long completedRepairCount = allRepairs.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
        long rejectedRepairCount = allRepairs.stream().filter(r -> "REJECTED".equals(r.getStatus())).count();
        
        stats.put("totalRepairCount", totalRepairCount);
        stats.put("pendingRepairCount", pendingRepairCount);
        stats.put("repairingRepairCount", repairingRepairCount);
        stats.put("completedRepairCount", completedRepairCount);
        stats.put("rejectedRepairCount", rejectedRepairCount);
        
        return stats;
    }
    
    public Map<String, Object> getScrapStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<ScrapRecord> allScraps = scrapRecordRepository.findAll();
        long totalScrapCount = allScraps.size();
        
        long pendingScrapCount = allScraps.stream().filter(s -> "PENDING".equals(s.getStatus())).count();
        long approvedScrapCount = allScraps.stream().filter(s -> "APPROVED".equals(s.getStatus())).count();
        long rejectedScrapCount = allScraps.stream().filter(s -> "REJECTED".equals(s.getStatus())).count();
        
        stats.put("totalScrapCount", totalScrapCount);
        stats.put("pendingScrapCount", pendingScrapCount);
        stats.put("approvedScrapCount", approvedScrapCount);
        stats.put("rejectedScrapCount", rejectedScrapCount);
        
        return stats;
    }
    
    public Map<String, Object> getUseStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<UseRecord> allRecords = useRecordRepository.findAll();
        long borrowedCount = allRecords.stream().filter(r -> "BORROWED".equals(r.getStatus())).count();
        long returnedCount = allRecords.stream().filter(r -> "RETURNED".equals(r.getStatus())).count();
        
        stats.put("totalUseCount", allRecords.size());
        stats.put("borrowedCount", borrowedCount);
        stats.put("returnedCount", returnedCount);
        
        return stats;
    }
    
    public Map<String, Object> getAllStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("device", getDeviceStatistics());
        result.put("repair", getRepairStatistics());
        result.put("scrap", getScrapStatistics());
        result.put("use", getUseStatistics());
        return result;
    }
}
