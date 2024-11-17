package com.uam_control_system.repository;

import com.uam_control_system.model.DroneRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DroneRouteRepository extends JpaRepository<DroneRoute, Long> {
    Optional<DroneRoute> findByInstanceId(int instanceId);
}
