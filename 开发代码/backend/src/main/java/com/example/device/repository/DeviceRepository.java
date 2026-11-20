package com.example.device.repository;

import com.example.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    boolean existsByDeviceCode(String deviceCode);
    Device findByDeviceCode(String deviceCode);
    
    @Query("SELECT d FROM Device d WHERE " +
           "(:deviceCode IS NULL OR d.deviceCode LIKE %:deviceCode%) AND " +
           "(:deviceName IS NULL OR d.deviceName LIKE %:deviceName%) AND " +
           "(:category IS NULL OR d.category = :category) AND " +
           "(:status IS NULL OR d.status = :status)")
    List<Device> findByConditions(
            @Param("deviceCode") String deviceCode,
            @Param("deviceName") String deviceName,
            @Param("category") String category,
            @Param("status") String status
    );
    
    @Query("SELECT d FROM Device d WHERE d.nextCalibrationDate <= :date")
    List<Device> findDueForCalibration(@Param("date") Date date);
    
    @Query("SELECT d FROM Device d WHERE d.status = 'SCRAP'")
    List<Device> findScrapDevices();
    
    @Query("SELECT d FROM Device d WHERE d.status != 'SCRAP'")
    List<Device> findActiveDevices();
}
