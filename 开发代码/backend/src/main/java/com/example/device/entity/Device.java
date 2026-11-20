package com.example.device.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "device")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "device_code", unique = true, nullable = false, length = 50)
    private String deviceCode;
    
    @Column(name = "device_name", nullable = false, length = 100)
    private String deviceName;
    
    @Column(length = 200)
    private String specification;
    
    @Column(length = 100)
    private String supplier;
    
    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;
    
    @Column(length = 100)
    private String manufacturer;
    
    @Column(name = "calibration_validity")
    private Date calibrationValidity;
    
    @Column(name = "next_calibration_date")
    private Date nextCalibrationDate;
    
    @Column(length = 20)
    private String status;
    
    @Column(length = 200)
    private String location;
    
    @Column(name = "purchase_date")
    private Date purchaseDate;
    
    @Column(length = 50)
    private String category;
    
    @Column(name = "device_type", length = 20)
    private String deviceType;
    
    @Column(name = "create_time")
    private Date createTime;
    
    @Column(name = "update_time")
    private Date updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        updateTime = new Date();
        if (status == null) {
            status = "IN_USE";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}
