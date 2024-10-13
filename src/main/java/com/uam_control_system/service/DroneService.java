package com.uam_control_system.service;

import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.model.Command;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneService {
    private final List<DroneInstance> droneInstances = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();

    // 시뮬레이터 URL
    private final String simulatorUrl = "";

    // 드론 생성 요청을 시뮬레이터에 전달하고 응답 받기
    public DroneInstance createDroneInstance(DroneInstance droneInstance, CommandService commandService) {
        // Command 객체 생성
        Command command = new Command(0, droneInstance.getId(), droneInstance.getLatitude(), droneInstance.getLongitude());

        // 시뮬레이터에 드론 생성 요청
        ResponseEntity<DroneInstance> response = restTemplate.postForEntity("http://simulator-url/drone/create", command, DroneInstance.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            // 응답에서 드론 인스턴스를 생성
            DroneInstance createdDrone = response.getBody();
            if (createdDrone != null) {
                droneInstances.add(createdDrone);
            }
            return createdDrone;
        }
        return null;
    }

    // 드론 삭제 메서드
    public boolean deleteDroneInstance(int instanceId, CommandService commandService) {
        // Command 객체 생성
        Command command = new Command(1, instanceId, 0, 0); // 위도와 경도는 필요 없으므로 0으로 설정

        // 시뮬레이터에 드론 삭제 요청
        ResponseEntity<Void> response = restTemplate.postForEntity("http://simulator-url/drone/delete", command, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            droneInstances.removeIf(drone -> drone.getId() == instanceId);
            return true;
        }
        return false;
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
