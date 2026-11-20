package com.example.device.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "purchase_total")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseTotal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String totalList;
    
    @Column(length = 20)
    private String status;
    
    @Column(name = "review_comment", length = 500)
    private String reviewComment;
    
    @Column(name = "reviewer_id")
    private Long reviewerId;
    
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
