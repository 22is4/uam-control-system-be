package com.uam_control_system.controller;

import com.uam_control_system.model.DroneInstance;
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

    // 드론 생성
    @PostMapping("/create")
    public ResponseEntity<String> createDrone(@RequestBody DroneInstance droneInstance) {
        DroneInstance newDrone = droneService.createDroneInstance(droneInstance, commandService);
        if (newDrone != null) {
            return ResponseEntity.ok("드론 생성 완료: " + newDrone.getId());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 생성 실패");
        }
    }

    // 드론 삭제
    @DeleteMapping("/delete/{instanceId}")
    public ResponseEntity<String> deleteDrone(@PathVariable int instanceId) {
        boolean success = droneService.deleteDroneInstance(instanceId, commandService);
        if (success) {
            return ResponseEntity.ok("드론 삭제 완료: " + instanceId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 삭제 실패");
        }
    }

    // 드론 정보 업데이트
    @PutMapping("/update/{instanceId}")
    public ResponseEntity<String> updateDroneInfo(@PathVariable int instanceId, @RequestBody DroneInstance updatedInfo) {
        droneService.updateDroneInfo(instanceId, updatedInfo);
        return ResponseEntity.ok("드론 정보 업데이트 완료");
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
