package com.example.device.service;

import com.example.device.dto.ScrapRecordDTO;
import com.example.device.dto.ScrapRequest;
import com.example.device.entity.Device;
import com.example.device.entity.ScrapRecord;
import com.example.device.entity.SysUser;
import com.example.device.repository.DeviceRepository;
import com.example.device.repository.ScrapRecordRepository;
import com.example.device.repository.SysUserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapService {
    private final ScrapRecordRepository scrapRecordRepository;
    private final DeviceRepository deviceRepository;
    private final SysUserRepository userRepository;
    
    public ScrapService(ScrapRecordRepository scrapRecordRepository,
                       DeviceRepository deviceRepository,
                       SysUserRepository userRepository) {
        this.scrapRecordRepository = scrapRecordRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }
    
    public ScrapRecord createScrap(ScrapRequest request, Long applicantId) {
        ScrapRecord record = new ScrapRecord();
        record.setDeviceId(request.getDeviceId());
        record.setApplicantId(applicantId);
        record.setReason(request.getReason());
        record.setStatus("PENDING");
        return scrapRecordRepository.save(record);
    }
    
    public ScrapRecord reviewScrap(Long id, String status, String comment, Long reviewerId) {
        ScrapRecord record = scrapRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("报废申请不存在"));
        record.setStatus(status);
        record.setReviewComment(comment);
        record.setReviewerId(reviewerId);
        
        if ("APPROVED".equals(status)) {
            record.setScrapDate(new Date());
            Device device = deviceRepository.findById(record.getDeviceId()).orElse(null);
            if (device != null) {
                device.setStatus("SCRAP");
                deviceRepository.save(device);
            }
        }
        
        return scrapRecordRepository.save(record);
    }
    
    public List<ScrapRecordDTO> getRecordsByApplicant(Long applicantId) {
        return scrapRecordRepository.findByApplicantId(applicantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ScrapRecordDTO> getPendingRecords() {
        return scrapRecordRepository.findByStatus("PENDING").stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ScrapRecordDTO> getAllRecords() {
        return scrapRecordRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private ScrapRecordDTO toDTO(ScrapRecord record) {
        ScrapRecordDTO dto = new ScrapRecordDTO();
        dto.setId(record.getId());
        dto.setDeviceId(record.getDeviceId());
        dto.setApplicantId(record.getApplicantId());
        dto.setReason(record.getReason());
        dto.setStatus(record.getStatus());
        dto.setReviewerId(record.getReviewerId());
        dto.setReviewComment(record.getReviewComment());
        dto.setScrapDate(record.getScrapDate());
        dto.setApplyDate(record.getCreateTime());
        dto.setUpdateTime(record.getUpdateTime());
        
        Device device = deviceRepository.findById(record.getDeviceId()).orElse(null);
        if (device != null) {
            dto.setDeviceName(device.getDeviceName());
            dto.setDeviceCode(device.getDeviceCode());
        }
        
        SysUser applicant = userRepository.findById(record.getApplicantId()).orElse(null);
        if (applicant != null) {
            dto.setApplicantName(applicant.getRealName());
        }
        
        if (record.getReviewerId() != null) {
            SysUser reviewer = userRepository.findById(record.getReviewerId()).orElse(null);
            if (reviewer != null) {
                dto.setReviewerName(reviewer.getRealName());
            }
        }
        
        return dto;
    }
}
