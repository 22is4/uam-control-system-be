package com.uam_control_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DroneInstance {
    private int instanceId; // 드론 인스턴스 번호
    private double latitude;
    private double longitude;
    private double altitude;
    private double vx;
    private double vy;
    private double vz;
    private double speed;
    private String status;
}
