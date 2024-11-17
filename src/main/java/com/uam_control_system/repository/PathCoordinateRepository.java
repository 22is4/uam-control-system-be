package com.uam_control_system.repository;

import com.uam_control_system.model.PathCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathCoordinateRepository extends JpaRepository<PathCoordinate, Long> {
}
