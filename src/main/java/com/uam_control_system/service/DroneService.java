package com.uam_control_system.service;

import com.uam_control_system.controller.CommandController;
import com.uam_control_system.model.DroneInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneService {
    private static final Logger logger = LoggerFactory.getLogger(CommandController.class);
    private final List<DroneInstance> droneInstances = new ArrayList<>();
//    private final DroneController droneController;
    private final RestTemplate restTemplate;
    @Value("${frontend.url}")
    private String frontendUrl;

    public DroneService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createDrone(DroneInstance droneInstance) {
        sendToFrontend("droneCreated", droneInstance);
    }

    // 프론트엔드로 드론 삭제 알림 전송
    public void deleteDrone(int instanceId) {
        sendToFrontend("droneDeleted", instanceId);
    }

    // 모든 드론 인스턴스를 반환하는 메서드
    public List<DroneInstance> getAllDroneInstances() {
        return new ArrayList<>(droneInstances); // 새로운 리스트로 반환
    }

    // 특정 드론 인스턴스 위치 정보를 반환하는 메서드
    public DroneInstance getDroneById(int instanceId) {
        return droneInstances.stream()
                .filter(drone -> drone.getInstanceId() == instanceId)
                .findFirst()
                .orElse(null);
    }

    // 프론트엔드로 알림 전송
    private void sendToFrontend(String type, Object data) {
        try {
            String url = frontendUrl + "/" + type;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Object> entity = new HttpEntity<>(data, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("프론트엔드로 전송 성공: {}", data);
            } else {
                logger.error("프론트엔드로 전송 실패. 응답 상태: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("프론트엔드로 전송 중 오류 발생: {}", e.getMessage());
        }
    }
}