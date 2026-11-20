package com.example.device.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "purchase_apply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "device_name", nullable = false, length = 100)
    private String deviceName;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "project_usage", length = 500)
    private String projectUsage;
    
    @Column(name = "applicant_id", nullable = false)
    private Long applicantId;
    
    @Column(length = 20)
    private String status;
    
    @Column(name = "review_comment", length = 500)
    private String reviewComment;
    
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
