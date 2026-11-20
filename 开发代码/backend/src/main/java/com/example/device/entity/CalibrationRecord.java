package com.example.device.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "calibration_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalibrationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "device_id", nullable = false)
    private Long deviceId;
    
    @Column(name = "calibration_date", nullable = false)
    private Date calibrationDate;
    
    @Column(name = "next_calibration_date")
    private Date nextCalibrationDate;
    
    @Column(name = "certificate_url", length = 500)
    private String certificateUrl;
    
    @Column(name = "calibration_organization", length = 100)
    private String calibrationOrganization;
    
    @Column(length = 20)
    private String result;
    
    @Column(name = "create_time")
    private Date createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
}
