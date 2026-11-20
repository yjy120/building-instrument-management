package com.example.device.service;

import com.example.device.dto.DeviceIssueReportRequest;
import com.example.device.entity.Device;
import com.example.device.entity.DeviceIssueReport;
import com.example.device.entity.RepairOrder;
import com.example.device.entity.ScrapRecord;
import com.example.device.repository.DeviceIssueReportRepository;
import com.example.device.repository.DeviceRepository;
import com.example.device.repository.RepairOrderRepository;
import com.example.device.repository.ScrapRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DeviceIssueReportService {
    
    private final DeviceIssueReportRepository reportRepository;
    private final DeviceRepository deviceRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final ScrapRecordRepository scrapRecordRepository;
    
    public DeviceIssueReportService(DeviceIssueReportRepository reportRepository,
                                   DeviceRepository deviceRepository,
                                   RepairOrderRepository repairOrderRepository,
                                   ScrapRecordRepository scrapRecordRepository) {
        this.reportRepository = reportRepository;
        this.deviceRepository = deviceRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.scrapRecordRepository = scrapRecordRepository;
    }
    
    public DeviceIssueReport createReport(DeviceIssueReportRequest request, Long applicantId) {
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("设备不存在"));
        
        DeviceIssueReport report = new DeviceIssueReport();
        report.setDeviceId(request.getDeviceId());
        report.setApplicantId(applicantId);
        report.setFaultDescription(request.getFaultDescription());
        report.setDeviceType(device.getDeviceType() != null ? device.getDeviceType() : "SMALL");
        report.setStatus("PENDING");
        report.setCreateTime(new Date());
        report.setUpdateTime(new Date());
        
        return reportRepository.save(report);
    }
    
    public List<DeviceIssueReport> getAllReports() {
        return reportRepository.findAll();
    }
    
    public List<DeviceIssueReport> getPendingReports() {
        return reportRepository.findByStatusOrderByCreateTimeDesc("PENDING");
    }
    
    public List<DeviceIssueReport> getReportsByApplicant(Long applicantId) {
        return reportRepository.findByApplicantId(applicantId);
    }
    
    @Transactional
    public DeviceIssueReport handleAsRepair(Long reportId, Long handlerId) {
        DeviceIssueReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("异常申请不存在"));
        
        if (!"PENDING".equals(report.getStatus())) {
            throw new RuntimeException("该申请已处理");
        }
        
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setDeviceId(report.getDeviceId());
        repairOrder.setApplicantId(report.getApplicantId());
        repairOrder.setFaultDescription(report.getFaultDescription());
        repairOrder.setStatus("PENDING");
        repairOrder.setCreateTime(new Date());
        repairOrder.setUpdateTime(new Date());
        
        RepairOrder savedRepair = repairOrderRepository.save(repairOrder);
        
        report.setStatus("PROCESSED");
        report.setHandleType("REPAIR");
        report.setHandleId(savedRepair.getId());
        report.setUpdateTime(new Date());
        
        return reportRepository.save(report);
    }
    
    @Transactional
    public DeviceIssueReport handleAsScrap(Long reportId, Long handlerId) {
        DeviceIssueReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("异常申请不存在"));
        
        if (!"PENDING".equals(report.getStatus())) {
            throw new RuntimeException("该申请已处理");
        }
        
        ScrapRecord scrapRecord = new ScrapRecord();
        scrapRecord.setDeviceId(report.getDeviceId());
        scrapRecord.setApplicantId(report.getApplicantId());
        scrapRecord.setReason(report.getFaultDescription());
        scrapRecord.setStatus("PENDING");
        scrapRecord.setCreateTime(new Date());
        scrapRecord.setUpdateTime(new Date());
        
        ScrapRecord savedScrap = scrapRecordRepository.save(scrapRecord);
        
        report.setStatus("PROCESSED");
        report.setHandleType("SCRAP");
        report.setHandleId(savedScrap.getId());
        report.setUpdateTime(new Date());
        
        return reportRepository.save(report);
    }
}
