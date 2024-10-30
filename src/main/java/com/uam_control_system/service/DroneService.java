package com.uam_control_system.service;

import com.uam_control_system.controller.CommandController;
import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.model.PathCoordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DroneService {
    private static final Logger logger = LoggerFactory.getLogger(CommandController.class);
    private final List<DroneInstance> droneInstances = new ArrayList<>();
    private final RestTemplate restTemplate;
    private final DroneRouteService droneRouteService;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public DroneService(@Lazy RestTemplate restTemplate, @Lazy DroneRouteService droneRouteService) {
        this.restTemplate = restTemplate;
        this.droneRouteService = droneRouteService;
    }

    public void createDrone(DroneInstance droneInstance) {
        // 드론 인스턴스를 리스트에 추가
        droneInstances.add(droneInstance);

        // 프론트엔드에 드론 생성 알림 전송
        sendToFrontend("create", droneInstance);
    }

    // 프론트엔드로 드론 삭제 알림 전송
    public void deleteDrone(int instanceId) {
        // 드론 인스턴스 리스트에서 해당 인스턴스를 찾아 삭제
        droneInstances.removeIf(drone -> drone.getInstanceId() == instanceId);

        // 프론트엔드에 드론 삭제 알림 전송
        sendToFrontend("delete", instanceId);
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

    // 드론 경로 데이터를 프론트엔드로 전송
    public void sendRouteToFrontend(int instanceId) {
        try {
            List<PathCoordinate> droneRoute = droneRouteService.getRoute(instanceId);
            if (droneRoute == null || droneRoute.isEmpty()) {
                logger.error("드론 인스턴스 ID {}에 대한 유효한 구간 데이터가 없습니다.", instanceId);
                return;
            }
            // 드론 ID와 좌표 리스트를 담을 Map 생성
            Map<String, Object> payload = new HashMap<>();
            payload.put("instanceId", instanceId);
            payload.put("coordinates", droneRoute);

            // 프론트엔드로 전송
            sendToFrontend("path", payload);
        } catch (IllegalArgumentException e) {
            logger.error("경로 추출 중 오류 발생: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("경로 전송 중 오류 발생: {}", e.getMessage());
        }
    }

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