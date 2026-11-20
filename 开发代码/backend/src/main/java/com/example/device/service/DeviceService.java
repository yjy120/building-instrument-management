package com.example.device.service;

import com.example.device.dto.DeviceRequest;
import com.example.device.entity.Device;
import com.example.device.entity.Stock;
import java.math.BigDecimal;
import com.example.device.repository.DeviceRepository;
import com.example.device.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final StockRepository stockRepository;
    
    public DeviceService(DeviceRepository deviceRepository, StockRepository stockRepository) {
        this.deviceRepository = deviceRepository;
        this.stockRepository = stockRepository;
    }
    
    private static final BigDecimal LARGE_DEVICE_THRESHOLD = new BigDecimal("5000");
    
    public Device createDevice(DeviceRequest request) {
        if (deviceRepository.existsByDeviceCode(request.getDeviceCode())) {
            throw new RuntimeException("仪器编码已存在");
        }
        
        Device device = new Device();
        device.setDeviceCode(request.getDeviceCode());
        device.setDeviceName(request.getDeviceName());
        device.setSpecification(request.getSpecification());
        device.setSupplier(request.getSupplier());
        device.setPurchasePrice(request.getPurchasePrice());
        device.setManufacturer(request.getManufacturer());
        device.setCalibrationValidity(request.getCalibrationValidity());
        device.setNextCalibrationDate(request.getNextCalibrationDate());
        device.setStatus(request.getStatus() != null ? request.getStatus() : "IN_USE");
        device.setLocation(request.getLocation());
        device.setPurchaseDate(request.getPurchaseDate());
        device.setCategory(request.getCategory());
        device.setDeviceType(determineDeviceType(request.getPurchasePrice()));
        
        Device savedDevice = deviceRepository.save(device);
        
        Stock stock = new Stock();
        stock.setDeviceId(savedDevice.getId());
        stock.setQuantity(0);
        stock.setThreshold(10);
        stockRepository.save(stock);
        
        return savedDevice;
    }
    
    public Device updateDevice(Long id, DeviceRequest request) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("仪器不存在"));
        
        if (!device.getDeviceCode().equals(request.getDeviceCode()) && 
            deviceRepository.existsByDeviceCode(request.getDeviceCode())) {
            throw new RuntimeException("仪器编码已存在");
        }
        
        device.setDeviceCode(request.getDeviceCode());
        device.setDeviceName(request.getDeviceName());
        device.setSpecification(request.getSpecification());
        device.setSupplier(request.getSupplier());
        device.setPurchasePrice(request.getPurchasePrice());
        device.setManufacturer(request.getManufacturer());
        device.setCalibrationValidity(request.getCalibrationValidity());
        device.setNextCalibrationDate(request.getNextCalibrationDate());
        device.setStatus(request.getStatus());
        device.setLocation(request.getLocation());
        device.setPurchaseDate(request.getPurchaseDate());
        device.setCategory(request.getCategory());
        device.setDeviceType(determineDeviceType(request.getPurchasePrice()));
        
        return deviceRepository.save(device);
    }
    
    private String determineDeviceType(BigDecimal purchasePrice) {
        if (purchasePrice == null) {
            return "SMALL";
        }
        return purchasePrice.compareTo(LARGE_DEVICE_THRESHOLD) >= 0 ? "LARGE" : "SMALL";
    }
    
    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new RuntimeException("仪器不存在");
        }
        deviceRepository.deleteById(id);
    }
    
    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }
    
    public Device getDeviceByCode(String code) {
        return deviceRepository.findByDeviceCode(code);
    }
    
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    
    public List<Device> searchDevices(String deviceCode, String deviceName, String category, String status) {
        return deviceRepository.findByConditions(deviceCode, deviceName, category, status);
    }
    
    public List<Device> findActiveDevices() {
        return deviceRepository.findActiveDevices();
    }
    
    public List<Device> findDueForCalibration() {
        return deviceRepository.findDueForCalibration(new Date());
    }
}
