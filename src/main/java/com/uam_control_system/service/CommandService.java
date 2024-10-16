package com.uam_control_system.service;

import com.uam_control_system.model.Command;
import com.uam_control_system.model.Coordinate;
import com.uam_control_system.model.DroneInstance;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommandService {
    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);

    // 드론 인스턴스를 관리하기 위한 Map
    private final Map<Integer, DroneInstance> droneInstances = new HashMap<>();

    // 드론 생성 명령 처리
    public DroneInstance createDrone(Command command) {
        logger.info("드론 생성 명령 수신: {}", command);
        if (command.getType() == 0) {
            DroneInstance createdDrone = new DroneInstance(
                    command.getInstanceId(),
                    command.getLatitude(),
                    command.getLongitude(),
                    0.0,  // 초기 고도
                    0.0,  // 초기 속도
                    "드론 생성 완료"
            );
            droneInstances.put(command.getInstanceId(), createdDrone);
            logger.info("드론 생성 성공: {}", createdDrone);
            return createdDrone;
        }
        logger.error("드론 생성 실패");
        return null;
    }

    // 드론 삭제 명령 처리
    public DroneInstance deleteDrone(Command command) {
        logger.info("드론 삭제 명령 수신: {}", command);
        if (command.getType() == 1) {
            DroneInstance removedDrone = droneInstances.remove(command.getInstanceId());
            if (removedDrone != null) {
                logger.info("드론 삭제 성공: {}", command.getInstanceId());
                return removedDrone;
            } else {
                logger.error("드론 삭제 실패: 드론 인스턴스 없음");
            }
        }
        return null;
    }

    // 드론 미션 수행 명령 처리
    public boolean executeMission(Command command) {
        if (command.getType() == 2) { // 미션 수행 명령
//            ResponseEntity<Void> response = restTemplate.postForEntity(simulatorUrl + "/mission", command, Void.class);
//            return response.getStatusCode().is2xxSuccessful(); // 성공 여부 반환
            logger.info("드론 미션 수신: {}", command.getMissionItems());
            return true;
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