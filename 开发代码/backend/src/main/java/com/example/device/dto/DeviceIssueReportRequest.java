package com.example.device.dto;

public class DeviceIssueReportRequest {
    
    private Long deviceId;
    private String faultDescription;

    public DeviceIssueReportRequest() {}

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }
}
