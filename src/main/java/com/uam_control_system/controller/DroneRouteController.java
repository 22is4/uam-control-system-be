package com.uam_control_system.controller;

import com.uam_control_system.dto.PathCoordinateDTO;
import com.uam_control_system.service.DroneRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DroneRouteController {

    private final DroneRouteService droneRouteService;

    @Autowired
    public DroneRouteController(DroneRouteService droneRouteService) {
        this.droneRouteService = droneRouteService;
    }

    @GetMapping("/uam/path/{instanceId}")
    public List<PathCoordinateDTO> getDroneRoute(@PathVariable int instanceId) {
        return droneRouteService.getRouteForInstance(instanceId);
    }
    @GetMapping("uam/drone/path/{instanceId}")
    public List<PathCoordinateDTO> getDronesRoutes(@PathVariable int instanceId) {
        return droneRouteService.getRouteForInstance(instanceId);
    }
}

