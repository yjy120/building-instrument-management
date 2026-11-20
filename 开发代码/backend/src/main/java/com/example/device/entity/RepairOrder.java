package com.example.device.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "repair_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "device_id", nullable = false)
    private Long deviceId;
    
    @Column(name = "applicant_id", nullable = false)
    private Long applicantId;
    
    @Column(name = "fault_description", length = 500)
    private String faultDescription;
    
    @Column(length = 20)
    private String status;
    
    @Column(name = "repair_date")
    private Date repairDate;
    
    @Column(name = "repair_cost", precision = 10, scale = 2)
    private BigDecimal repairCost;
    
    @Column(name = "repair_comment", length = 500)
    private String repairComment;
    
    @Column(name = "create_time")
    private Date createTime;
    
    @Column(name = "update_time")
    private Date updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        updateTime = new Date();
        if (status == null) {
            status = "PENDING";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}
