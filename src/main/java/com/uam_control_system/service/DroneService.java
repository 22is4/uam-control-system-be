package com.uam_control_system.service;

import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.model.Command;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;

@Service
public class DroneService {
    private final List<DroneInstance> droneInstances = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String frontendUrl = "http://frontend-service-url/uam"; // 프론트엔드 URL

    // 드론 생성 시 프론트엔드에 알림 전송
    public void notifyDroneCreated(DroneInstance droneInstance) {
        String url = frontendUrl + "/drone/created";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<DroneInstance> request = new HttpEntity<>(droneInstance, headers);

            // 요청 수행 및 응답 처리
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("드론 생성 알림 전송 성공: " + response.getBody());
            } else {
                System.err.println("드론 생성 알림 전송 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("드론 생성 알림 중 예외 발생: " + e.getMessage());
        }
    }

    // 드론 삭제 시 프론트엔드에 알림 전송
    public void notifyDroneDeleted(int instanceId) {
        String url = frontendUrl + "/drone/deleted/" + instanceId;
        try {
            // 요청 수행 및 응답 처리
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("드론 삭제 알림 전송 성공: " + response.getBody());
            } else {
                System.err.println("드론 삭제 알림 전송 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("드론 삭제 알림 중 예외 발생: " + e.getMessage());
        }
    }

    // 모든 드론 인스턴스를 반환하는 메서드
    public List<DroneInstance> getAllDroneInstances() {
        return new ArrayList<>(droneInstances); // 새로운 리스트로 반환
    }

    // 특정 드론 인스턴스 위치 정보를 반환하는 메서드
    public DroneInstance getDroneById(int instanceId) {
        return droneInstances.stream()
                .filter(drone -> drone.getId() == instanceId)
                .findFirst()
                .orElse(null);
    }
}