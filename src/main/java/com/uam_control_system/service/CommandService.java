package com.uam_control_system.service;

import com.uam_control_system.model.Command;
import com.uam_control_system.model.DroneInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommandService {
    private final RestTemplate restTemplate = new RestTemplate();
    // 시뮬레이터의 명령 API URL
    private final String simulatorUrl = "http://simulator-url/command";

    // 드론 생성 명령 처리
    public DroneInstance createDrone(Command command) {
        if (command.getType() == 0) { // 드론 생성 명령
            // 시뮬레이터에 드론 생성 요청
            ResponseEntity<DroneInstance> response = restTemplate.postForEntity(simulatorUrl + "/create", command, DroneInstance.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody(); // 생성된 드론 반환
            }
        }
        return null; // 실패 시 null 반환
    }

    // 드론 삭제 명령 처리
    public boolean deleteDrone(Command command) {
        if (command.getType() == 1) { // 드론 삭제 명령
            ResponseEntity<Void> response = restTemplate.postForEntity(simulatorUrl + "/delete", command, Void.class);
            return response.getStatusCode().is2xxSuccessful(); // 성공 여부 반환
        }
        return false; // 실패 시 false 반환
    }

    // 드론 미션 수행 명령 처리
    public boolean executeMission(Command command) {
        if (command.getType() == 2) { // 미션 수행 명령
            ResponseEntity<Void> response = restTemplate.postForEntity(simulatorUrl + "/mission", command, Void.class);
            return response.getStatusCode().is2xxSuccessful(); // 성공 여부 반환
        }
        return false; // 실패 시 false 반환
    }
}
