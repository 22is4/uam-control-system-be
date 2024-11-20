package com.uam_control_system.repository;

import com.uam_control_system.model.DroneRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRouteRepository extends JpaRepository<DroneRoute, Long> {
}

