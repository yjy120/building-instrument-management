package com.example.device.controller;

import com.example.device.dto.CalibrationRequest;
import com.example.device.entity.CalibrationRecord;
import com.example.device.service.CalibrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calibration")
public class CalibrationController {
    private final CalibrationService calibrationService;
    
    public CalibrationController(CalibrationService calibrationService) {
        this.calibrationService = calibrationService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<CalibrationRecord>> getAllRecords() {
        return ResponseEntity.ok(calibrationService.getAllRecords());
    }
    
    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<CalibrationRecord>> getRecordsByDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(calibrationService.getRecordsByDevice(deviceId));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCalibration(@RequestBody CalibrationRequest request) {
        try {
            CalibrationRecord record = calibrationService.createCalibration(request);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
