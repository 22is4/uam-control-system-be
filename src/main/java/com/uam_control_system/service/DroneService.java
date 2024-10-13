package com.uam_control_system.service;

import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.model.MissionItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneService {
    private final List<DroneInstance> droneInstances = new ArrayList<>();

    // 드론 인스턴스를 추가하는 메서드
    public DroneInstance addDroneInstance(int instanceId, double latitude, double longitude) {
        DroneInstance newDrone = new DroneInstance(instanceId, latitude, longitude, 0.0, "대기 중");
        droneInstances.add(newDrone);
        return newDrone;
    }

    // 드론 삭제 메서드
    public void deleteDroneInstance(int instanceId) {
        droneInstances.removeIf(drone -> drone.getId() == instanceId);
    }

    // 미션 수행 메서드
    public void assignMission(int instanceId, List<MissionItem> missionItems) {
        // 드론 인스턴스 찾기
        DroneInstance droneInstance = getDroneById(instanceId);
        if (droneInstance != null) {
            // 미션 아이템을 드론에 할당
//            droneInstance.setMissionItems(missionItems);
            System.out.println("Assigning mission to drone: " + instanceId);
            // 미션 수행 로직 추가 필요
        }
    }

    // 모든 드론 인스턴스를 반환하는 메서드
    public List<DroneInstance> getAllDroneInstances() {
        return new ArrayList<>(droneInstances); // 새로운 리스트로 반환
    }

    // 특정 드론 인스턴스 위치 정보를 반환하는 메서드
    public DroneInstance getDroneById(int instanceId) {
        return droneInstances.stream()
                .filter(drone -> drone.getId() == instanceId)
                .findFirst()
                .orElse(null);
    }

    // 드론 비행 정보 업데이트 메서드
    public void updateDroneInfo(int instanceId, DroneInstance updatedInfo) {
        droneInstances.stream()
                .filter(drone -> drone.getId() == instanceId)
                .findFirst()
                .ifPresent(droneInstance -> {
                    droneInstance.setLatitude(updatedInfo.getLatitude());
                    droneInstance.setLongitude(updatedInfo.getLongitude());
                    droneInstance.setAltitude(updatedInfo.getAltitude());
                    droneInstance.setStatus(updatedInfo.getStatus());
                });
    }
}
