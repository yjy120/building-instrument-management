package com.example.device.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "use_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "device_id", nullable = false)
    private Long deviceId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "project_name", length = 200)
    private String projectName;
    
    @Column(name = "borrow_time", nullable = false)
    private Date borrowTime;
    
    @Column(name = "return_time")
    private Date returnTime;
    
    @Column(length = 20)
    private String status;
    
    @Column(name = "create_time")
    private Date createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        if (status == null) {
            status = "BORROWED";
        }
    }
}
