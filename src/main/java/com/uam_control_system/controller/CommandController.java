package com.uam_control_system.controller;

import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.model.Command;
import com.uam_control_system.model.Coordinate;
import com.uam_control_system.model.MissionItem;
import com.uam_control_system.service.CommandService;
import com.uam_control_system.service.DroneService;
import com.uam_control_system.service.DroneMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/uam/command")
public class CommandController {
    private static final Logger logger = LoggerFactory.getLogger(CommandController.class);
    private final CommandService commandService;
    private final DroneService droneService;
    private final DroneMissionService droneMissionService;


    @Autowired
    public CommandController(CommandService commandService, @Lazy DroneService droneService, DroneMissionService droneMissionService) {
        this.commandService = commandService;
        this.droneService = droneService;
        this.droneMissionService = droneMissionService;
    }

    // 드론 생성 요청 처리
    @PostMapping("/create")
    public ResponseEntity<String> createDrone(@RequestBody Command command) {
        logger.info("드론 생성 요청 처리: {}", command);

        // 출발지 저장
        droneMissionService.saveStartLocation(command);

        DroneInstance createdDrone = commandService.createDrone(command);

        if (createdDrone != null) {
            return ResponseEntity.ok("드론 생성 완료"); // 성공 응답 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 생성 실패"); // 실패 응답 반환
        }
    }

    // 드론 삭제 요청 처리
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDrone(@RequestBody Command command) {
        logger.info("드론 삭제 요청 처리: {}", command);
        DroneInstance removedDrone = commandService.deleteDrone(command);

        if (removedDrone != null) {
            return ResponseEntity.ok("드론 삭제 완료"); // 성공 응답 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 삭제 실패"); // 실패 응답 반환
        }
    }

    // 미션 수행 요청 처리
    @PostMapping("/mission")
    public ResponseEntity<String> executeMission(@RequestBody Command command) {
        logger.info("미션 수행 요청: {}", command);

        if (command.getType() == 2) {
            List<MissionItem> missionItems = command.getMissionItems();

            if (missionItems == null || missionItems.isEmpty()) {
                logger.error("미션 아이템이 없습니다: {}", command);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("미션 아이템이 없습니다");
            }

            // 도착지 저장
            droneMissionService.saveEndLocation(command);
            boolean success = droneMissionService.executeMission(command);
            if (success) {
                return ResponseEntity.ok("미션 수행 요청 완료");
            } else {
                logger.error("미션 수행 실패: {}", command);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("미션 수행 요청 실패");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 명령 타입");
    }

    // 드론 위치 업데이트 요청 처리
    @PostMapping("/update")
    public ResponseEntity<String> updateCoordinate(@RequestBody Coordinate coordinate) {
        logger.info("드론 위치 업데이트 요청 처리: {}", coordinate);
        boolean success = commandService.updateDroneStatus(coordinate);
        if (success) {
            return ResponseEntity.ok("드론 위치 업데이트 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 위치 업데이트 실패");
        }
    }
}
