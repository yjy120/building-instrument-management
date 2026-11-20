package com.example.device.service;

import com.example.device.dto.UseRecordRequest;
import com.example.device.entity.UseRecord;
import com.example.device.repository.UseRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UseRecordService {
    private final UseRecordRepository useRecordRepository;
    
    public UseRecordService(UseRecordRepository useRecordRepository) {
        this.useRecordRepository = useRecordRepository;
    }
    
    public UseRecord createUseRecord(UseRecordRequest request, Long userId) {
        UseRecord record = new UseRecord();
        record.setDeviceId(request.getDeviceId());
        record.setUserId(userId);
        record.setProjectName(request.getProjectName());
        record.setBorrowTime(new Date());
        record.setStatus("BORROWED");
        return useRecordRepository.save(record);
    }
    
    public UseRecord returnDevice(Long id) {
        UseRecord record = useRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("领用记录不存在"));
        record.setReturnTime(new Date());
        record.setStatus("RETURNED");
        return useRecordRepository.save(record);
    }
    
    public List<UseRecord> getRecordsByUser(Long userId) {
        return useRecordRepository.findByUserId(userId);
    }
    
    public List<UseRecord> getRecordsByDevice(Long deviceId) {
        return useRecordRepository.findByDeviceId(deviceId);
    }
    
    public List<UseRecord> getBorrowedRecords() {
        return useRecordRepository.findByStatus("BORROWED");
    }
    
    public List<UseRecord> getAllRecords() {
        return useRecordRepository.findAll();
    }
}
