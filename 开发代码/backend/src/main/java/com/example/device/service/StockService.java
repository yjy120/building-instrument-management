package com.example.device.service;

import com.example.device.entity.Device;
import com.example.device.entity.Stock;
import com.example.device.entity.StockRecord;
import com.example.device.repository.DeviceRepository;
import com.example.device.repository.StockRecordRepository;
import com.example.device.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final StockRecordRepository stockRecordRepository;
    private final DeviceRepository deviceRepository;
    
    public StockService(StockRepository stockRepository, StockRecordRepository stockRecordRepository,
                       DeviceRepository deviceRepository) {
        this.stockRepository = stockRepository;
        this.stockRecordRepository = stockRecordRepository;
        this.deviceRepository = deviceRepository;
    }
    
    public Stock getStockByDeviceId(Long deviceId) {
        return stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("库存记录不存在"));
    }
    
    public Stock updateStock(Long deviceId, Integer quantity, String reason, Long operatorId) {
        Stock stock = stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("库存记录不存在"));
        
        stock.setQuantity(quantity);
        Stock savedStock = stockRepository.save(stock);
        
        StockRecord record = new StockRecord();
        record.setDeviceId(deviceId);
        record.setType(quantity > stock.getQuantity() ? "IN" : "OUT");
        record.setQuantity(Math.abs(quantity - stock.getQuantity()));
        record.setReason(reason);
        record.setOperatorId(operatorId);
        stockRecordRepository.save(record);
        
        return savedStock;
    }
    
    public Stock adjustStock(Long deviceId, Integer delta, String reason, Long operatorId) {
        Stock stock = stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("库存记录不存在"));
        
        int newQuantity = stock.getQuantity() + delta;
        if (newQuantity < 0) {
            throw new RuntimeException("库存不足");
        }
        
        stock.setQuantity(newQuantity);
        Stock savedStock = stockRepository.save(stock);
        
        StockRecord record = new StockRecord();
        record.setDeviceId(deviceId);
        record.setType(delta > 0 ? "IN" : "OUT");
        record.setQuantity(Math.abs(delta));
        record.setReason(reason);
        record.setOperatorId(operatorId);
        stockRecordRepository.save(record);
        
        return savedStock;
    }
    
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    
    public List<Stock> getLowStockAlerts() {
        return stockRepository.findByQuantityLessThan(10);
    }
    
    public Stock updateThreshold(Long deviceId, Integer threshold) {
        Stock stock = stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("库存记录不存在"));
        stock.setThreshold(threshold);
        return stockRepository.save(stock);
    }
    
    public List<StockRecord> getStockRecords(Long deviceId) {
        return stockRecordRepository.findByDeviceId(deviceId);
    }
}
