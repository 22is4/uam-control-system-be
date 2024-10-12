package com.uam_control_system.service;

import com.uam_control_system.model.DroneInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneService {
    private final List<DroneInstance> droneInstances = new ArrayList<>();
    private int nextId = 1;

    // 모든 드론 인스턴스를 반환하는 메서드
    public List<DroneInstance> getAllDroneInstances() {
        return new ArrayList<>(droneInstances); // 새로운 리스트로 반환
    }

    // 특정 드론 인스턴스 위치 정보를 반환하는 메서드
    public DroneInstance getDroneById(int id) {
        return droneInstances.stream()
                .filter(drone -> drone.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // 드론 비행 정보 업데이트 메서드
    public void updateDroneInfo(int id, DroneInstance updatedInfo) {
        droneInstances.stream()
                .filter(drone -> drone.getId() == id)
                .findFirst()
                .ifPresent(droneInstance -> {
                    droneInstance.setLatitude(updatedInfo.getLatitude());
                    droneInstance.setLongitude(updatedInfo.getLongitude());
                    droneInstance.setAltitude(updatedInfo.getAltitude());
                    droneInstance.setStatus(updatedInfo.getStatus());
                });
    }
}
