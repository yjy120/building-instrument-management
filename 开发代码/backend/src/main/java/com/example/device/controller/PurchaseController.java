package com.example.device.controller;

import com.example.device.dto.PurchaseApplyRequest;
import com.example.device.entity.PurchaseApply;
import com.example.device.entity.PurchaseTotal;
import com.example.device.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }
    
    @GetMapping("/apply")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<PurchaseApply>> getAllApplies() {
        return ResponseEntity.ok(purchaseService.getAllApplies());
    }
    
    @GetMapping("/apply/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<PurchaseApply>> getPendingApplies() {
        return ResponseEntity.ok(purchaseService.getPendingApplies());
    }
    
    @GetMapping("/apply/user/{userId}")
    public ResponseEntity<List<PurchaseApply>> getAppliesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(purchaseService.getAppliesByApplicant(userId));
    }
    
    @PostMapping("/apply")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createApply(@RequestBody PurchaseApplyRequest request, @RequestParam Long userId) {
        try {
            PurchaseApply apply = purchaseService.createApply(request, userId);
            return ResponseEntity.ok(apply);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/apply/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> updateApplyStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String comment) {
        try {
            PurchaseApply apply = purchaseService.updateApplyStatus(id, status, comment);
            return ResponseEntity.ok(apply);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/total")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<PurchaseTotal>> getAllTotals() {
        return ResponseEntity.ok(purchaseService.getAllTotals());
    }
    
    @PostMapping("/total")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> createTotal(@RequestBody List<Long> applyIds) {
        try {
            PurchaseTotal total = purchaseService.createTotal(applyIds);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/total/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> reviewTotal(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String comment,
            @RequestParam Long reviewerId) {
        try {
            PurchaseTotal total = purchaseService.reviewTotal(id, status, comment, reviewerId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
