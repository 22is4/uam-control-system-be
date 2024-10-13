package com.uam_control_system.controller;

import com.uam_control_system.model.Command;
import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.service.CommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uam/command")
public class CommandController {
    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    // 드론 생성 요청 처리
    @PostMapping("/create")
    public ResponseEntity<String> createDrone(@RequestBody Command command) {
        DroneInstance createdDrone = commandService.createDrone(command);
        if (createdDrone != null) {
            return ResponseEntity.ok("드론 생성 완료: " + createdDrone.getId());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 생성 실패");
        }
    }

    // 드론 삭제 요청 처리
    @PostMapping("/delete")
    public ResponseEntity<String> deleteDrone(@RequestBody Command command) {
        boolean success = commandService.deleteDrone(command);
        if (success) {
            return ResponseEntity.ok("드론 삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("드론 삭제 실패");
        }
    }

    // 미션 수행 요청 처리
    @PostMapping("/mission")
    public ResponseEntity<String> executeMission(@RequestBody Command command) {
        boolean success = commandService.executeMission(command);
        if (success) {
            return ResponseEntity.ok("미션 수행 요청 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("미션 수행 요청 실패");
        }
    }
}
