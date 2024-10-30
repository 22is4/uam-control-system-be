package com.uam_control_system.service;

import com.uam_control_system.model.Coordinate;
import com.uam_control_system.model.PathCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneRouteService {

    private final Logger logger = LoggerFactory.getLogger(DroneRouteService.class);
    private final List<PathCoordinate> fullRoute;
    private final DroneMissionService droneMissionService;

    @Autowired
    public DroneRouteService(@Lazy DroneMissionService droneMissionService) {
        this.droneMissionService = droneMissionService;
        this.fullRoute = new ArrayList<>();

        // 노선 추가
        fullRoute.add(new PathCoordinate(35.8714, 128.6014, 100)); // 출발지
        fullRoute.add(new PathCoordinate(35.8855, 128.6108, 100));
        fullRoute.add(new PathCoordinate(35.8897, 128.5952, 100));
        fullRoute.add(new PathCoordinate(35.8719, 128.5853, 100));
        fullRoute.add(new PathCoordinate(35.8665, 128.6137, 100));
        fullRoute.add(new PathCoordinate(35.8600, 128.5900, 100));
        fullRoute.add(new PathCoordinate(35.8750, 128.6200, 100));
        fullRoute.add(new PathCoordinate(35.8805, 128.6305, 100)); // 종점
    }

    // 특정 드론의 출발지와 목적지 사이 구간 추출
    public List<PathCoordinate> getRoute(int instanceId) {
        logger.info("getRoute called with instanceId: {}", instanceId);

        Coordinate startCoordinate = droneMissionService.getStartLocation(instanceId);
        Coordinate endCoordinate = droneMissionService.getEndLocation(instanceId);

        logger.info("Start Coordinate: {}, End Coordinate: {}", startCoordinate, endCoordinate);

        if (startCoordinate == null || endCoordinate == null) {
            logger.error("유효하지 않은 출발지 또는 목적지입니다.");
            throw new IllegalArgumentException("유효하지 않은 출발지 또는 목적지입니다.");
        }

        int startIndex = findClosestCoordinateIndex(startCoordinate);
        int endIndex = findClosestCoordinateIndex(endCoordinate);

        logger.info("Start Index: {}, End Index: {}", startIndex, endIndex);

        if (startIndex < 0 || endIndex < 0 || startIndex >= endIndex) {
            logger.error("유효하지 않은 출발지 또는 목적지 인덱스입니다.");
            throw new IllegalArgumentException("유효하지 않은 출발지 또는 목적지 인덱스입니다.");
        }

        // 정해진 구간만 반환
        List<PathCoordinate> routeSegment = fullRoute.subList(startIndex, endIndex + 1);
        logger.info("Route : {}", routeSegment);
        return routeSegment;
    }

    // 가장 가까운 경유지 인덱스 찾기
    private int findClosestCoordinateIndex(Coordinate point) {
        logger.info("findClosestCoordinateIndex called with point: {}", point);
        double minDistance = Double.MAX_VALUE;
        int closestIndex = -1;

        for (int i = 0; i < fullRoute.size(); i++) {
            double distance = calculateDistance(point, fullRoute.get(i));
            logger.debug("Distance to PathCoordinate[{}]: {}", i, distance);
            if (distance < minDistance) {
                minDistance = distance;
                closestIndex = i;
            }
        }
        logger.info("Closest index found: {}", closestIndex);
        return closestIndex;
    }

    // 좌표 간 거리 계산 (단순 유클리드 거리 계산 예시)
    private double calculateDistance(Coordinate p1, PathCoordinate p2) {
        double latDiff = p1.getLatitude() - p2.getLatitude();
        double lonDiff = p1.getLongitude() - p2.getLongitude();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }
}
