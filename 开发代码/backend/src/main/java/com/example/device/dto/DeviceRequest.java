package com.example.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    private String deviceCode;
    private String deviceName;
    private String specification;
    private String supplier;
    private BigDecimal purchasePrice;
    private String manufacturer;
    private Date calibrationValidity;
    private Date nextCalibrationDate;
    private String status;
    private String location;
    private Date purchaseDate;
    private String category;
}
