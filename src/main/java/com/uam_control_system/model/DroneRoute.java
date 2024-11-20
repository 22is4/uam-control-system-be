package com.uam_control_system.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "drone_routes")
@Data
public class DroneRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id", nullable = false)
    private int routeId; // 경로 ID

    @Column(name = "latitude", nullable = false)
    private double latitude; // 위도

    @Column(name = "longitude", nullable = false)
    private double longitude; // 경도

    @Column(name = "altitude", nullable = false)
    private int altitude; // 고도

    @Column(name = "waypoint_order", nullable = false)
    private int waypointOrder; // 경로 내 순서
}
