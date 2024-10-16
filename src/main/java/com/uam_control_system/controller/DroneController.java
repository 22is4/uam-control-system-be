package com.uam_control_system.controller;

import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.model.Command;
import com.uam_control_system.service.DroneService;
import com.uam_control_system.service.CommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uam/drone")
public class DroneController {
    private final DroneService droneService;
    private final CommandService commandService;

    public DroneController(DroneService droneService, CommandService commandService) {
        this.droneService = droneService;
        this.commandService = commandService;
    }

    // 드론 생성 - CommandService를 통해 드론 생성 요청을 시뮬레이터로 보냄
    @PostMapping("/create")
    public ResponseEntity<String> createDrone(@RequestBody Command command) {
        DroneInstance newDrone = commandService.createDrone(command);  // Command 객체를 통해 드론 생성
        if (newDrone != null) {
            return ResponseEntity.ok("드론 생성 완료: " + newDrone.getId());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 생성 실패");
        }
    }

    // 드론 삭제 - CommandService를 통해 드론 삭제 요청을 시뮬레이터로 보냄
    @DeleteMapping("/delete/{instanceId}")
    public ResponseEntity<String> deleteDrone(@PathVariable int instanceId) {
        Command command = new Command();
        command.setType(1);  // 삭제 명령
        command.setInstanceId(instanceId);
        boolean success = commandService.deleteDrone(command);  // Command 객체를 통해 드론 삭제
        if (success) {
            return ResponseEntity.ok("드론 삭제 완료: " + instanceId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 삭제 실패");
        }
    }

    // 모든 드론 인스턴스 조회
    @GetMapping("/instances")
    public ResponseEntity<List<DroneInstance>> getAllDroneInstances() {
        List<DroneInstance> instances = droneService.getAllDroneInstances();
        return ResponseEntity.ok(instances);
    }

    // 특정 드론 인스턴스 조회
    @GetMapping("/instances/{instanceId}")
    public ResponseEntity<DroneInstance> getDroneInstanceById(@PathVariable int instanceId) {
        DroneInstance instance = droneService.getDroneById(instanceId);
        if (instance != null) {
            return ResponseEntity.ok(instance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}