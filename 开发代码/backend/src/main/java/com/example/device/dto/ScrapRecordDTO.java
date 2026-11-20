package com.example.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapRecordDTO {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String deviceCode;
    private Long applicantId;
    private String applicantName;
    private String reason;
    private String status;
    private Long reviewerId;
    private String reviewerName;
    private String reviewComment;
    private Date scrapDate;
    private Date applyDate;
    private Date updateTime;
}