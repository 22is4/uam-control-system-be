package com.uam_control_system.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PathCoordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;
    private double altitude;

    @ManyToOne
    @JoinColumn(name = "drone_route_id")
    private DroneRoute droneRoute;

    // 추가적인 생성자 (droneRoute를 포함하는 생성자)
    public PathCoordinate(double latitude, double longitude, double altitude, DroneRoute droneRoute) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.droneRoute = droneRoute;
    }
}
