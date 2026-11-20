package com.example.device.controller;

import com.example.device.dto.ScrapRecordDTO;
import com.example.device.dto.ScrapRequest;
import com.example.device.entity.ScrapRecord;
import com.example.device.service.ScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scrap")
public class ScrapController {
    private final ScrapService scrapService;
    
    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<ScrapRecordDTO>> getAllRecords() {
        return ResponseEntity.ok(scrapService.getAllRecords());
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ScrapRecordDTO>> getPendingRecords() {
        return ResponseEntity.ok(scrapService.getPendingRecords());
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TESTER')")
    public ResponseEntity<List<ScrapRecordDTO>> getRecordsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(scrapService.getRecordsByApplicant(userId));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createScrap(@RequestBody ScrapRequest request, @RequestParam Long userId) {
        try {
            ScrapRecord record = scrapService.createScrap(request, userId);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> reviewScrap(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String comment,
            @RequestParam Long reviewerId) {
        try {
            ScrapRecord record = scrapService.reviewScrap(id, status, comment, reviewerId);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
