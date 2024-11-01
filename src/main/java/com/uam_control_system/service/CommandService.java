package com.uam_control_system.service;

import com.uam_control_system.model.Command;
import com.uam_control_system.model.Coordinate;
import com.uam_control_system.model.DroneInstance;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommandService {
    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);

    private final DroneService droneService;
    private final DroneInstanceRepository droneInstanceRepository;

    @Autowired
    public CommandService(DroneService droneService, DroneInstanceRepository droneInstanceRepository) {
        this.droneService = droneService;
        this.droneInstanceRepository = droneInstanceRepository;
    }

    // 드론 인스턴스를 관리하기 위한 Map
//    private final Map<Integer, DroneInstance> droneInstances = new HashMap<>();

    // 드론 생성 명령 처리
    public DroneInstance createDrone(Command command) {
        logger.info("드론 생성 명령 수신: {}", command);
        if (command.getType() == 0) {
            DroneInstance createdDrone = new DroneInstance(
                    command.getInstanceId(),
                    command.getLatitude(),
                    command.getLongitude(),
                    0.0,  // 초기 고도
                    0.0,
                    0.0,
                    0.0,
                    0.0,  // 초기 속도
                    "드론 생성 완료"
            );
            droneInstanceRepository.addDrone(createdDrone);
            droneService.sendToFrontend("create", createdDrone);
            logger.info("드론 생성 성공: {}", createdDrone);
            return createdDrone;
        }
        logger.error("드론 생성 실패: 명령 타입 오류");
        return null;
    }

    // 드론 삭제 명령 처리
    public DroneInstance deleteDrone(Command command) {
        logger.info("드론 삭제 명령 수신: {}", command);
        if (command.getType() == 1) {
            DroneInstance removedDrone = droneInstanceRepository.getDroneById(command.getInstanceId());
            if (removedDrone != null) {
                droneInstanceRepository.removeDrone(command.getInstanceId());
                droneService.sendToFrontend("delete", command.getInstanceId());
                logger.info("드론 삭제 성공: {}", command.getInstanceId());
                return removedDrone;
            } else {
                logger.error("드론 삭제 실패: 드론 인스턴스 없음");
            }
        }
        return null;
    }

    public boolean updateDroneStatus(Coordinate coordinate) {
        logger.info("드론 위치 업데이트 명령 수신: {}", coordinate);

        DroneInstance droneInstance = droneInstanceRepository.getDroneById(coordinate.getInstanceId());
        if (droneInstance != null) {
            droneInstance.setLatitude(coordinate.getLatitude());
            droneInstance.setLongitude(coordinate.getLongitude());
            droneInstance.setAltitude(coordinate.getAltitude());
            droneInstance.setVx(coordinate.getVx());
            droneInstance.setVy(coordinate.getVy());
            droneInstance.setVz(coordinate.getVz());
            droneInstance.setSpeed(coordinate.getSpeed());
            droneInstance.setStatus(coordinate.getStatus());
            logger.info("드론 위치 업데이트 완료: {}", droneInstance);
//            droneService.sendToFrontend("update", droneInstance);
            return true;
        } else {
            logger.error("드론 인스턴스 없음: {}", coordinate.getInstanceId());
            return false;
        }
    }
}