package com.uam_control_system.controller;

import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uam")
public class DroneController {
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
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

    // 시뮬레이터로부터 드론 정보 업데이트 요청 처리
    @PostMapping("/update-drone/{id}")
    public ResponseEntity<String> updateDroneInfo(@PathVariable int id, @RequestBody DroneInstance updatedInfo) {
        droneService.updateDroneInfo(id, updatedInfo);
        return ResponseEntity.ok("드론 정보 업데이트 완료");
    }
}
