package com.example.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairOrderDTO {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String deviceCode;
    private Long applicantId;
    private String applicantName;
    private String problemDescription;
    private String status;
    private Date repairDate;
    private BigDecimal repairCost;
    private String repairComment;
    private Date applyDate;
    private Date updateTime;
}