package com.example.device.controller;

import com.example.device.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    
    private final ReportService reportService;
    
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    @GetMapping("/device")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> getDeviceStatistics() {
        return ResponseEntity.ok(reportService.getDeviceStatistics());
    }
    
    @GetMapping("/repair")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> getRepairStatistics() {
        return ResponseEntity.ok(reportService.getRepairStatistics());
    }
    
    @GetMapping("/scrap")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> getScrapStatistics() {
        return ResponseEntity.ok(reportService.getScrapStatistics());
    }
    
    @GetMapping("/use")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> getUseStatistics() {
        return ResponseEntity.ok(reportService.getUseStatistics());
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> getAllStatistics() {
        return ResponseEntity.ok(reportService.getAllStatistics());
    }
}
