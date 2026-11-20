package com.example.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseApplyRequest {
    private String deviceName;
    private Integer quantity;
    private String projectUsage;
}
