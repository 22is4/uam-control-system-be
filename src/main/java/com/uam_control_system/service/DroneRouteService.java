package com.uam_control_system.service;

import com.uam_control_system.model.Coordinate;
import com.uam_control_system.model.DroneRoute;
import com.uam_control_system.model.PathCoordinate;
import com.uam_control_system.dto.PathCoordinateDTO;
import com.uam_control_system.repository.DroneRouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DroneRouteService {

    private final Logger logger = LoggerFactory.getLogger(DroneRouteService.class);
    private final List<List<PathCoordinateDTO>> allRoutes;
    private final Map<Integer, List<PathCoordinateDTO>> droneRouteMap;

    private final DroneMissionService droneMissionService;
    private final DroneRouteRepository droneRouteRepository;

    @Autowired
    public DroneRouteService(@Lazy DroneMissionService droneMissionService, DroneRouteRepository droneRouteRepository) {
        this.droneMissionService = droneMissionService;
        this.droneRouteRepository = droneRouteRepository;
        this.allRoutes = new ArrayList<>();
        this.droneRouteMap = new HashMap<>();
    }

    // 특정 드론의 출발지와 목적지 사이 구간 추출
    public List<PathCoordinateDTO> getRoute(int instanceId) {
        logger.info("getRoute called with instanceId: {}", instanceId);

        // 데이터베이스에서 경로 로드
        List<List<PathCoordinate>> routes = getAllRoutes();

        Coordinate startCoordinate = droneMissionService.getStartLocation(instanceId);
        Coordinate endCoordinate = droneMissionService.getEndLocation(instanceId);

        logger.info("Start Coordinate: {}, End Coordinate: {}", startCoordinate, endCoordinate);

        if (startCoordinate == null || endCoordinate == null) {
            logger.error("유효하지 않은 출발지 또는 목적지입니다.");
            throw new IllegalArgumentException("유효하지 않은 출발지 또는 목적지입니다.");
        }

        // 가장 가까운 노선을 찾고 해당 노선의 경유지 추출
        List<PathCoordinate> nearestRoute = findNearestRoute(routes, startCoordinate); // 변경됨
        int startIndex = findClosestCoordinateIndex(startCoordinate, nearestRoute);
        int endIndex = findClosestCoordinateIndex(endCoordinate, nearestRoute);

        logger.info("Start Index: {}, End Index: {}", startIndex, endIndex);

        if (startIndex < 0 || endIndex < 0 || startIndex >= nearestRoute.size() || endIndex >= nearestRoute.size()) {
            logger.error("유효하지 않은 출발지 또는 목적지 인덱스입니다.");
            throw new IllegalArgumentException("유효하지 않은 출발지 또는 목적지 인덱스입니다.");
        }

        // 정해진 구간만 반환 (startIndex와 endIndex의 관계에 따라 방향을 다르게 설정)
        List<PathCoordinate> routeSegment;
        if (startIndex <= endIndex) {
            routeSegment = nearestRoute.subList(startIndex, endIndex + 1);
        } else {
            // startIndex가 endIndex보다 큰 경우, 역방향으로 구간을 설정
            routeSegment = nearestRoute.subList(endIndex, startIndex + 1);
            // 역방향으로 추출된 구간을 반대로 정렬하여 올바른 순서로 반환
            Collections.reverse(routeSegment);
        }

        List<PathCoordinateDTO> final_route = convertToDTO(routeSegment);
        droneRouteMap.put(instanceId, final_route);
        logger.info("Route Segment: {}", final_route);

        return final_route;
    }

    // 데이터베이스에서 전체 경로 가져오기
    private List<List<PathCoordinate>> getAllRoutes() {
        logger.info("Fetching all routes from database.");

        // 데이터베이스에서 모든 DroneRoute를 가져오기
        List<DroneRoute> allRouteEntities = droneRouteRepository.findAll();

        // route_id별로 경로를 그룹화
        Map<Integer, List<PathCoordinate>> routeMap = new HashMap<>();
        for (DroneRoute entity : allRouteEntities) {
            routeMap.computeIfAbsent(entity.getRouteId(), k -> new ArrayList<>())
                    .add(new PathCoordinate(entity)); // DroneRoute 생성자를 사용
        }

        // 그룹화된 경로를 List로 변환
        return new ArrayList<>(routeMap.values());
    }

    private List<PathCoordinate> findNearestRoute(List<List<PathCoordinate>> routes, Coordinate point) {
        double minDistance = Double.MAX_VALUE;
        List<PathCoordinate> nearestRoute = null;

        for (List<PathCoordinate> route : routes) {
            double distance = calculateDistance(point, route.get(0));
            if (distance < minDistance) {
                minDistance = distance;
                nearestRoute = route;
            }
        }
        return nearestRoute;
    }

    private int findClosestCoordinateIndex(Coordinate point, List<PathCoordinate> route) {
        logger.info("findClosestCoordinateIndex called with point: {}", point);
        double minDistance = Double.MAX_VALUE;
        int closestIndex = -1;

        for (int i = 0; i < route.size(); i++) {
            double distance = calculateDistance(point, route.get(i));
            logger.debug("Distance to PathCoordinate[{}]: {}", i, distance);
            if (distance < minDistance) {
                minDistance = distance;
                closestIndex = i;
            }
        }
        logger.info("Closest index found: {}", closestIndex);
        return closestIndex;
    }

    // 좌표 간 거리 계산 (단순 유클리드 거리 계산)
    private double calculateDistance(Coordinate p1, PathCoordinate p2) {
        double latDiff = p1.getLatitude() - p2.getLatitude();
        double lonDiff = p1.getLongitude() - p2.getLongitude();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }

    // PathCoordinate 리스트를 PathCoordinateDTO 리스트로 변환
    private List<PathCoordinateDTO> convertToDTO(List<PathCoordinate> pathCoordinates) {
        List<PathCoordinateDTO> dtoList = new ArrayList<>();
        for (PathCoordinate path : pathCoordinates) {
            dtoList.add(new PathCoordinateDTO(path.getLatitude(), path.getLongitude(), path.getAltitude()));
        }
        return dtoList;
    }

    // 경로 요청 처리
    public List<PathCoordinateDTO> getRouteForInstance(int instanceId) {
        logger.info("getRouteForInstance called with instanceId: {}", instanceId);

        List<PathCoordinateDTO> routeSegment = droneRouteMap.get(instanceId);
        if (routeSegment == null) {
            logger.error("드론 인스턴스 번호 {}에 대한 경로가 존재하지 않습니다.", instanceId);
            throw new IllegalArgumentException("드론 인스턴스 번호에 대한 경로가 존재하지 않습니다.");
        }

        logger.info("Returning route segment for instanceId {}: {}", instanceId, routeSegment);
        return routeSegment;
    }
}
