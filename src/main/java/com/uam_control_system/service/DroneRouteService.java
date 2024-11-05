package com.uam_control_system.service;

import com.uam_control_system.model.Coordinate;
import com.uam_control_system.model.PathCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DroneRouteService {

    private final Logger logger = LoggerFactory.getLogger(DroneRouteService.class);
    private final List<List<PathCoordinate>> allRoutes;
    private final Map<Integer, List<PathCoordinate>> droneRouteMap;

    private final DroneMissionService droneMissionService;

    @Autowired
    public DroneRouteService(@Lazy DroneMissionService droneMissionService) {
        this.droneMissionService = droneMissionService;
        this.allRoutes = new ArrayList<>();
        this.droneRouteMap = new HashMap<>();

        // 각 노선을 개별 리스트로 추가
        List<PathCoordinate> route1 = new ArrayList<>();
        route1.add(new PathCoordinate(35.8429, 128.5668, 450)); // 대구 가톨릭대학교 병원 남문 주차장
        route1.add(new PathCoordinate(35.8513, 128.5581, 450)); // 두류 공원 주차장
        route1.add(new PathCoordinate(35.8571, 128.5577, 450)); // 경유지
        route1.add(new PathCoordinate(35.8664, 128.5876, 450)); // 경유지
        route1.add(new PathCoordinate(35.8643, 128.5981, 450)); // 경유지
        route1.add(new PathCoordinate(35.8632, 128.6025, 450)); // 경유지
        route1.add(new PathCoordinate(35.8671, 128.6037, 450)); // 경유지
        route1.add(new PathCoordinate(35.8667, 128.6053, 450)); // 경북대학교 병원
        route1.add(new PathCoordinate(35.8668, 128.6061, 450)); // 경유지
        route1.add(new PathCoordinate(35.8653, 128.6132, 450)); // 신천(경유지)
        route1.add(new PathCoordinate(35.8678, 128.6143, 450)); // 경유지
        route1.add(new PathCoordinate(35.8735, 128.6118, 450)); // 경유지
        route1.add(new PathCoordinate(35.8778, 128.6041, 450)); // 경유지
        route1.add(new PathCoordinate(35.8793, 128.5956, 450)); // 경유지
        route1.add(new PathCoordinate(35.8827, 128.5968, 450)); // 삼성 창조 캠퍼스 정류장
        route1.add(new PathCoordinate(35.8889, 128.5987, 450)); // 경유지
        route1.add(new PathCoordinate(35.8881, 128.6065, 450)); // 경북대학교 대운동장
        route1.add(new PathCoordinate(35.8879, 128.6020, 450)); // 경유지
        route1.add(new PathCoordinate(35.8899, 128.6043, 450)); // 경유지
        route1.add(new PathCoordinate(35.8923, 128.6091, 450)); // 경유지
        route1.add(new PathCoordinate(35.89751, 128.61504, 450)); // 경유지
        route1.add(new PathCoordinate(35.89980, 128.60955, 450)); // 경유지
        route1.add(new PathCoordinate(35.90160, 128.60704, 450)); // 경유지
        route1.add(new PathCoordinate(35.90351, 128.60815, 450)); // 경유지
        route1.add(new PathCoordinate(35.90563, 128.61279, 450)); // 경유지
        route1.add(new PathCoordinate(35.90782, 128.61131, 450)); // 경유지
        route1.add(new PathCoordinate(35.90881, 128.61310, 450)); // 경유지
        route1.add(new PathCoordinate(35.90969, 128.61250, 450)); // 경유지
        route1.add(new PathCoordinate(35.90975, 128.61275, 450)); // 엑스코 인근 주차장
        allRoutes.add(route1);

        List<PathCoordinate> route2 = new ArrayList<>();
        route2.add(new PathCoordinate(35.85240, 128.49025, 450)); // 계명대학교 주차장
        route2.add(new PathCoordinate(35.85139, 128.49227, 450)); // 경유지
        route2.add(new PathCoordinate(35.85203, 128.50359, 450)); // 경유지
        route2.add(new PathCoordinate(35.85172, 128.50602, 450)); // 경유지
        route2.add(new PathCoordinate(35.85151, 128.50599, 450)); // 성서주유소
        route2.add(new PathCoordinate(35.85056, 128.51542, 450)); // 경유지
        route2.add(new PathCoordinate(35.85010, 128.52032, 450)); // 경유지
        route2.add(new PathCoordinate(35.86303, 128.52593, 450)); // 경유지
        route2.add(new PathCoordinate(35.86835, 128.53050, 450)); // 경유지
        route2.add(new PathCoordinate(35.87187, 128.56282, 450)); // 경유지
        route2.add(new PathCoordinate(35.87228, 128.57114, 450)); // 경유지
        route2.add(new PathCoordinate(35.87172, 128.57754, 450)); // 서문주차장
        route2.add(new PathCoordinate(35.87030, 128.57953, 450)); // 경유지
        route2.add(new PathCoordinate(35.86779, 128.58259, 450)); // 계명대학교 동산병원 주차장
        route2.add(new PathCoordinate(35.86257, 128.57518, 450)); // 대구 중부 소방서
        route2.add(new PathCoordinate(35.87523, 128.59517, 450)); // 대구역
        route2.add(new PathCoordinate(35.88489, 128.65105, 450)); // 동촌유원지 주차장
        route2.add(new PathCoordinate(35.89928, 128.63742, 450)); // 대구국제공항
        allRoutes.add(route2);
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

        // 가장 가까운 노선을 찾고 해당 노선의 경유지 추출
        List<PathCoordinate> nearestRoute = findNearestRoute(startCoordinate);
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
        droneRouteMap.put(instanceId, routeSegment);
        logger.info("Route Segment: {}", routeSegment);
        return routeSegment;
    }

    private List<PathCoordinate> findNearestRoute(Coordinate point) {
        double minDistance = Double.MAX_VALUE;
        List<PathCoordinate> nearestRoute = null;

        for (List<PathCoordinate> route : allRoutes) {
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

    // 시뮬레이터의 경로 요청 처리
    public List<PathCoordinate> getRouteForInstance(int instanceId) {
        logger.info("getRouteForInstance called with instanceId: {}", instanceId);

        List<PathCoordinate> routeSegment = droneRouteMap.get(instanceId);
        if (routeSegment == null) {
            logger.error("드론 인스턴스 번호 {}에 대한 경로가 존재하지 않습니다.", instanceId);
            throw new IllegalArgumentException("드론 인스턴스 번호에 대한 경로가 존재하지 않습니다.");
        }

        logger.info("Returning route segment for instanceId {}: {}", instanceId, routeSegment);
        return routeSegment;
    }

}
