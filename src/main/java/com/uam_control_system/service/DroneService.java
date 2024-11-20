package com.uam_control_system.service;

import com.uam_control_system.controller.CommandController;
import com.uam_control_system.model.DroneInstance;
import com.uam_control_system.model.PathCoordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DroneService {
    private static final Logger logger = LoggerFactory.getLogger(CommandController.class);
    private final RestTemplate restTemplate;
    private final DroneRouteService droneRouteService;
    private final DroneInstanceRepository droneInstanceRepository;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public DroneService(RestTemplate restTemplate,
                        DroneRouteService droneRouteService,
                        DroneInstanceRepository droneInstanceRepository) {
        this.restTemplate = restTemplate;
        this.droneRouteService = droneRouteService;
        this.droneInstanceRepository = droneInstanceRepository;
    }

    // 데이터베이스에 드론 저장
    public void saveDroneToRepository(DroneInstance droneInstance) {
        droneInstanceRepository.addDrone(droneInstance);
    }

    // 데이터베이스에서 드론 삭제
    public void deleteDroneFromRepository(int instanceId) {
        droneInstanceRepository.removeDrone(instanceId);
    }

    // 모든 드론 인스턴스를 반환하는 메서드
    public List<DroneInstance> getAllDroneInstances() {
        return droneInstanceRepository.getAllDrones();
    }

    // 특정 드론 인스턴스 위치 정보를 반환하는 메서드
    public DroneInstance getDroneById(int instanceId) {
        return droneInstanceRepository.getDroneById(instanceId);
    }
}