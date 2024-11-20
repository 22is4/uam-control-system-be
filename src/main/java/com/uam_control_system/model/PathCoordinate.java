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

    // DroneRoute로부터 PathCoordinate를 생성하는 생성자 추가
    public PathCoordinate(DroneRoute droneRoute) {
        this.latitude = droneRoute.getLatitude();
        this.longitude = droneRoute.getLongitude();
        this.altitude = droneRoute.getAltitude();
        this.droneRoute = droneRoute;
    }
}
