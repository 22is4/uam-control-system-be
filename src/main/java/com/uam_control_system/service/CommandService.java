package com.uam_control_system.service;

import com.uam_control_system.model.Command;
import com.uam_control_system.model.Coordinate;
import com.uam_control_system.model.DroneInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommandService {
    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    // 시뮬레이터의 명령 API URL
    private final String simulatorUrl = "http://localhost:8080/command";

    // 드론 인스턴스를 관리하기 위한 Map
    private final Map<String, DroneInstance> droneInstances = new HashMap<>();

    private final DroneService droneService;

    public CommandService(DroneService droneService) {
        this.droneService = droneService;
    }

    // 드론 생성 명령 처리
    public DroneInstance createDrone(Command command) {
        logger.info("드론 생성 명령 수신: {}", command);
        if (command.getType() == 0) { // 드론 생성 명령
            // 시뮬레이터에 드론 생성 요청
            ResponseEntity<DroneInstance> response = restTemplate.postForEntity(simulatorUrl + "/create", command, DroneInstance.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                DroneInstance createdDrone = response.getBody(); // 생성된 드론 인스턴스
                logger.info("드론 생성 성공: {}", createdDrone);
                // 드론 생성 후 프론트엔드에 알림 전송
                droneService.notifyDroneCreated(createdDrone); // 생성된 드론 전달
                return createdDrone; // 생성된 드론 반환
            }
        }
        logger.error("드론 생성 실패");
        return null; // 실패 시 null 반환
    }

    // 드론 삭제 명령 처리
    public boolean deleteDrone(Command command) {
        logger.info("드론 삭제 명령 수신: {}", command);
        if (command.getType() == 1) {
            ResponseEntity<Void> response = restTemplate.postForEntity(simulatorUrl + "/delete", command, Void.class);
            boolean success = response.getStatusCode().is2xxSuccessful();
            if (success) {
                logger.info("드론 삭제 성공");
                // 드론 삭제 후 프론트엔드에 알림 전송
                droneService.notifyDroneDeleted(command.getInstanceId());
            } else {
                logger.error("드론 삭제 실패");
            }
            return success;
        }
        return false;
    }

    // 드론 미션 수행 명령 처리
    public boolean executeMission(Command command) {
        if (command.getType() == 2) { // 미션 수행 명령
            ResponseEntity<Void> response = restTemplate.postForEntity(simulatorUrl + "/mission", command, Void.class);
            return response.getStatusCode().is2xxSuccessful(); // 성공 여부 반환
        }
        return false; // 실패 시 false 반환
    }
    public boolean updateDroneStatus(Coordinate coordinate) {
        logger.info("드론 위치 업데이트 명령 수신: {}", coordinate);

        // 인스턴스 ID를 기반으로 드론 인스턴스 조회
        DroneInstance droneInstance = droneInstances.get(coordinate.getInstanceId());
        if (droneInstance != null) {
            // 드론 인스턴스 업데이트
            droneInstance.setLatitude(coordinate.getLatitude());
            droneInstance.setLongitude(coordinate.getLongitude());
            droneInstance.setAltitude(coordinate.getAltitude());
            droneInstance.setSpeed(coordinate.getSpeed());
            droneInstance.setStatus(coordinate.getStatus());
            logger.info("드론 위치 업데이트 완료: {}", droneInstance);
            return true;
        } else {
            logger.error("드론 인스턴스 없음: {}", coordinate.getInstanceId());
            return false; // 드론 인스턴스가 존재하지 않음
        }
    }
}