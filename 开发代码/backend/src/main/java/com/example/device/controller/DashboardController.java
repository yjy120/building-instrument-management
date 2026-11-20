package com.example.device.controller;

import com.example.device.entity.*;
import com.example.device.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DeviceRepository deviceRepository;
    private final StockRepository stockRepository;
    private final UseRecordRepository useRecordRepository;
    private final CalibrationRecordRepository calibrationRecordRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final ScrapRecordRepository scrapRecordRepository;
    private final PurchaseApplyRepository purchaseApplyRepository;
    
    public DashboardController(DeviceRepository deviceRepository,
                              StockRepository stockRepository,
                              UseRecordRepository useRecordRepository,
                              CalibrationRecordRepository calibrationRecordRepository,
                              RepairOrderRepository repairOrderRepository,
                              ScrapRecordRepository scrapRecordRepository,
                              PurchaseApplyRepository purchaseApplyRepository) {
        this.deviceRepository = deviceRepository;
        this.stockRepository = stockRepository;
        this.useRecordRepository = useRecordRepository;
        this.calibrationRecordRepository = calibrationRecordRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.scrapRecordRepository = scrapRecordRepository;
        this.purchaseApplyRepository = purchaseApplyRepository;
    }
    
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        try {
            Map<String, Object> overview = new HashMap<>();
            
            long totalDevices = deviceRepository.count();
            long activeDevices = deviceRepository.findActiveDevices().size();
            long borrowedDevices = useRecordRepository.findByStatus("BORROWED").size();
            long lowStockCount = stockRepository.findByQuantityLessThan(10).size();
            long pendingPurchase = purchaseApplyRepository.findByStatus("PENDING").size();
            long pendingRepair = repairOrderRepository.findByStatus("PENDING").size();
            long pendingScrap = scrapRecordRepository.findByStatus("PENDING").size();
            long calibrationDue = deviceRepository.findDueForCalibration(new Date()).size();
            
            overview.put("totalDevices", totalDevices);
            overview.put("activeDevices", activeDevices);
            overview.put("borrowedDevices", borrowedDevices);
            overview.put("lowStockCount", lowStockCount);
            overview.put("pendingPurchase", pendingPurchase);
            overview.put("pendingRepair", pendingRepair);
            overview.put("pendingScrap", pendingScrap);
            overview.put("calibrationDue", calibrationDue);
            
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("type", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/calibration-due-list")
    public ResponseEntity<List<Device>> getCalibrationDueList() {
        return ResponseEntity.ok(deviceRepository.findDueForCalibration(new Date()));
    }
    
    @GetMapping("/low-stock-list")
    public ResponseEntity<List<Stock>> getLowStockList() {
        return ResponseEntity.ok(stockRepository.findByQuantityLessThan(10));
    }
    
    @GetMapping("/recent-records")
    public ResponseEntity<Map<String, Object>> getRecentRecords() {
        Map<String, Object> records = new HashMap<>();
        
        List<UseRecord> recentUse = useRecordRepository.findAll().stream()
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .limit(10)
                .collect(java.util.stream.Collectors.toList());
        
        List<CalibrationRecord> recentCalibration = calibrationRecordRepository.findAll().stream()
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .limit(10)
                .collect(java.util.stream.Collectors.toList());
        
        List<RepairOrder> recentRepair = repairOrderRepository.findAll().stream()
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .limit(10)
                .collect(java.util.stream.Collectors.toList());
        
        records.put("recentUse", recentUse);
        records.put("recentCalibration", recentCalibration);
        records.put("recentRepair", recentRepair);
        
        return ResponseEntity.ok(records);
    }
}
