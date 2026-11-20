package com.example.device.controller;

import com.example.device.dto.RepairOrderDTO;
import com.example.device.dto.RepairRequest;
import com.example.device.entity.RepairOrder;
import com.example.device.service.RepairService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repair")
public class RepairController {
    private final RepairService repairService;
    
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<RepairOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(repairService.getAllOrders());
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<RepairOrderDTO>> getPendingOrders() {
        return ResponseEntity.ok(repairService.getPendingOrders());
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TESTER')")
    public ResponseEntity<List<RepairOrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(repairService.getOrdersByApplicant(userId));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<?> createRepair(@RequestBody RepairRequest request, @RequestParam Long userId) {
        try {
            RepairOrder order = repairService.createRepair(request, userId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> updateRepairStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String comment) {
        try {
            RepairOrder order = repairService.updateRepairStatus(id, status, comment);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
