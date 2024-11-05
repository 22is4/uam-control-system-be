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
    private final RestTemplate restTemplate;
    private final DroneRouteService droneRouteService;
    private final DroneInstanceRepository droneInstanceRepository;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public DroneService(RestTemplate restTemplate,
                        DroneRouteService droneRouteService,
                        DroneInstanceRepository droneInstanceRepository) {
        this.restTemplate = restTemplate;
        this.droneRouteService = droneRouteService;
        this.droneInstanceRepository = droneInstanceRepository;
    }

    // 데이터베이스에 드론 저장
    public void saveDroneToRepository(DroneInstance droneInstance) {
        droneInstanceRepository.addDrone(droneInstance);
    }

    // 데이터베이스에서 드론 삭제
    public void deleteDroneFromRepository(int instanceId) {
        droneInstanceRepository.removeDrone(instanceId);
    }

    // 모든 드론 인스턴스를 반환하는 메서드
    public List<DroneInstance> getAllDroneInstances() {
        return droneInstanceRepository.getAllDrones();
    }

    // 특정 드론 인스턴스 위치 정보를 반환하는 메서드
    public DroneInstance getDroneById(int instanceId) {
        return droneInstanceRepository.getDroneById(instanceId);
    }

    // 드론 경로 데이터를 프론트엔드로 전송
    public void sendRoute(int instanceId) {
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

    public void sendToFrontend(String type, Object data) {
        try {
            String url = frontendUrl + "/uam/" + type;
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