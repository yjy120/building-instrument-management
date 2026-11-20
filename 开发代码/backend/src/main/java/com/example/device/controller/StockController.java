package com.example.device.controller;

import com.example.device.entity.Stock;
import com.example.device.entity.StockRecord;
import com.example.device.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;
    
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }
    
    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getStockByDeviceId(@PathVariable Long deviceId) {
        try {
            return ResponseEntity.ok(stockService.getStockByDeviceId(deviceId));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/records/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<StockRecord>> getStockRecords(@PathVariable Long deviceId) {
        return ResponseEntity.ok(stockService.getStockRecords(deviceId));
    }
    
    @PostMapping("/adjust")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adjustStock(
            @RequestParam Long deviceId,
            @RequestParam Integer delta,
            @RequestParam(required = false) String reason,
            @RequestParam Long operatorId) {
        try {
            Stock stock = stockService.adjustStock(deviceId, delta, reason, operatorId);
            return ResponseEntity.ok(stock);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/threshold")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateThreshold(
            @RequestParam Long deviceId,
            @RequestParam Integer threshold) {
        try {
            Stock stock = stockService.updateThreshold(deviceId, threshold);
            return ResponseEntity.ok(stock);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
