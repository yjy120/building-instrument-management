package com.example.device.service;

import com.example.device.dto.RepairOrderDTO;
import com.example.device.dto.RepairRequest;
import com.example.device.entity.Device;
import com.example.device.entity.RepairOrder;
import com.example.device.entity.SysUser;
import com.example.device.repository.DeviceRepository;
import com.example.device.repository.RepairOrderRepository;
import com.example.device.repository.SysUserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairService {
    private final RepairOrderRepository repairOrderRepository;
    private final DeviceRepository deviceRepository;
    private final SysUserRepository userRepository;
    
    public RepairService(RepairOrderRepository repairOrderRepository,
                        DeviceRepository deviceRepository,
                        SysUserRepository userRepository) {
        this.repairOrderRepository = repairOrderRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }
    
    public RepairOrder createRepair(RepairRequest request, Long applicantId) {
        RepairOrder order = new RepairOrder();
        order.setDeviceId(request.getDeviceId());
        order.setApplicantId(applicantId);
        order.setFaultDescription(request.getFaultDescription());
        order.setStatus("PENDING");
        
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("仪器不存在"));
        device.setStatus("REPAIRING");
        deviceRepository.save(device);
        
        return repairOrderRepository.save(order);
    }
    
    public RepairOrder updateRepairStatus(Long id, String status, String comment) {
        RepairOrder order = repairOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("维修工单不存在"));
        order.setStatus(status);
        order.setRepairComment(comment);
        
        if ("COMPLETED".equals(status)) {
            order.setRepairDate(new Date());
            Device device = deviceRepository.findById(order.getDeviceId()).orElse(null);
            if (device != null) {
                device.setStatus("IN_USE");
                deviceRepository.save(device);
            }
        }
        
        return repairOrderRepository.save(order);
    }
    
    public List<RepairOrderDTO> getOrdersByApplicant(Long applicantId) {
        return repairOrderRepository.findByApplicantId(applicantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<RepairOrderDTO> getPendingOrders() {
        return repairOrderRepository.findByStatus("PENDING").stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<RepairOrderDTO> getAllOrders() {
        return repairOrderRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private RepairOrderDTO toDTO(RepairOrder order) {
        RepairOrderDTO dto = new RepairOrderDTO();
        dto.setId(order.getId());
        dto.setDeviceId(order.getDeviceId());
        dto.setApplicantId(order.getApplicantId());
        dto.setProblemDescription(order.getFaultDescription());
        dto.setStatus(order.getStatus());
        dto.setRepairDate(order.getRepairDate());
        dto.setRepairCost(order.getRepairCost());
        dto.setRepairComment(order.getRepairComment());
        dto.setApplyDate(order.getCreateTime());
        dto.setUpdateTime(order.getUpdateTime());
        
        Device device = deviceRepository.findById(order.getDeviceId()).orElse(null);
        if (device != null) {
            dto.setDeviceName(device.getDeviceName());
            dto.setDeviceCode(device.getDeviceCode());
        }
        
        SysUser user = userRepository.findById(order.getApplicantId()).orElse(null);
        if (user != null) {
            dto.setApplicantName(user.getRealName());
        }
        
        return dto;
    }
}
