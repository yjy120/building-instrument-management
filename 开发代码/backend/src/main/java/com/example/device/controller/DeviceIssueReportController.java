package com.example.device.controller;

import com.example.device.dto.DeviceIssueReportRequest;
import com.example.device.entity.DeviceIssueReport;
import com.example.device.service.DeviceIssueReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/issue-report")
public class DeviceIssueReportController {
    
    private final DeviceIssueReportService reportService;
    
    public DeviceIssueReportController(DeviceIssueReportService reportService) {
        this.reportService = reportService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<DeviceIssueReport>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<DeviceIssueReport>> getPendingReports() {
        return ResponseEntity.ok(reportService.getPendingReports());
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TESTER')")
    public ResponseEntity<List<DeviceIssueReport>> getReportsByApplicant(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getReportsByApplicant(userId));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<?> createReport(@RequestBody DeviceIssueReportRequest request, @RequestParam Long userId) {
        try {
            DeviceIssueReport report = reportService.createReport(request, userId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/{id}/handle-repair")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> handleAsRepair(@PathVariable Long id, @RequestParam Long handlerId) {
        try {
            DeviceIssueReport report = reportService.handleAsRepair(id, handlerId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/{id}/handle-scrap")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> handleAsScrap(@PathVariable Long id, @RequestParam Long handlerId) {
        try {
            DeviceIssueReport report = reportService.handleAsScrap(id, handlerId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
