package com.uam_control_system.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DroneRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "droneRoute", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PathCoordinate> pathCoordinates;

    private int instanceId;

    // 추가적인 생성자 (instanceId만 사용하는 생성자)
    public DroneRoute(int instanceId) {
        this.instanceId = instanceId;
    }
}