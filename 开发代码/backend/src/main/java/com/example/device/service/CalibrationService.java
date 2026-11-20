package com.example.device.service;

import com.example.device.dto.CalibrationRequest;
import com.example.device.entity.CalibrationRecord;
import com.example.device.entity.Device;
import com.example.device.repository.CalibrationRecordRepository;
import com.example.device.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalibrationService {
    private final CalibrationRecordRepository calibrationRecordRepository;
    private final DeviceRepository deviceRepository;
    
    public CalibrationService(CalibrationRecordRepository calibrationRecordRepository,
                             DeviceRepository deviceRepository) {
        this.calibrationRecordRepository = calibrationRecordRepository;
        this.deviceRepository = deviceRepository;
    }
    
    public CalibrationRecord createCalibration(CalibrationRequest request) {
        CalibrationRecord record = new CalibrationRecord();
        record.setDeviceId(request.getDeviceId());
        record.setCalibrationDate(request.getCalibrationDate());
        record.setNextCalibrationDate(request.getNextCalibrationDate());
        record.setCertificateUrl(request.getCertificateUrl());
        record.setCalibrationOrganization(request.getCalibrationOrganization());
        record.setResult(request.getResult());
        
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("仪器不存在"));
        device.setNextCalibrationDate(request.getNextCalibrationDate());
        deviceRepository.save(device);
        
        return calibrationRecordRepository.save(record);
    }
    
    public List<CalibrationRecord> getRecordsByDevice(Long deviceId) {
        return calibrationRecordRepository.findByDeviceId(deviceId);
    }
    
    public List<CalibrationRecord> getAllRecords() {
        return calibrationRecordRepository.findAll();
    }
}
