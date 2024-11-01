package com.uam_control_system.service;

import com.uam_control_system.model.DroneInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneInstanceRepository {
    private final List<DroneInstance> droneInstances = new ArrayList<>();

    public void addDrone(DroneInstance droneInstance) {
        droneInstances.add(droneInstance);
    }

    public void removeDrone(int instanceId) {
        droneInstances.removeIf(drone -> drone.getInstanceId() == instanceId);
    }

    public List<DroneInstance> getAllDrones() {
        return new ArrayList<>(droneInstances);
    }

    public DroneInstance getDroneById(int instanceId) {
        return droneInstances.stream()
                .filter(drone -> drone.getInstanceId() == instanceId)
                .findFirst()
                .orElse(null);
    }
}
