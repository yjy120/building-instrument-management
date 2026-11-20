package com.example.device.controller;

import com.example.device.dto.UseRecordRequest;
import com.example.device.entity.UseRecord;
import com.example.device.service.UseRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/use-record")
public class UseRecordController {
    private final UseRecordService useRecordService;
    
    public UseRecordController(UseRecordService useRecordService) {
        this.useRecordService = useRecordService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UseRecord>> getAllRecords() {
        return ResponseEntity.ok(useRecordService.getAllRecords());
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TESTER')")
    public ResponseEntity<List<UseRecord>> getRecordsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(useRecordService.getRecordsByUser(userId));
    }
    
    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UseRecord>> getRecordsByDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(useRecordService.getRecordsByDevice(deviceId));
    }
    
    @GetMapping("/borrowed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UseRecord>> getBorrowedRecords() {
        return ResponseEntity.ok(useRecordService.getBorrowedRecords());
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<?> createUseRecord(@RequestBody UseRecordRequest request, @RequestParam Long userId) {
        try {
            UseRecord record = useRecordService.createUseRecord(request, userId);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/return/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<?> returnDevice(@PathVariable Long id) {
        try {
            UseRecord record = useRecordService.returnDevice(id);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
