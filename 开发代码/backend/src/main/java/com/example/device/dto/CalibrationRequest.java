package com.example.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalibrationRequest {
    private Long deviceId;
    private Date calibrationDate;
    private Date nextCalibrationDate;
    private String certificateUrl;
    private String calibrationOrganization;
    private String result;
}
