package com.uam_control_system.service;

import com.uam_control_system.model.Command;
import com.uam_control_system.model.Coordinate;
import com.uam_control_system.model.MissionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DroneMissionService {

    private static final Logger logger = LoggerFactory.getLogger(DroneMissionService.class);

    // 출발지와 목적지 정보를 저장할 맵
    private final Map<Integer, Coordinate> startLocations = new HashMap<>();
    private final Map<Integer, Coordinate> endLocations = new HashMap<>();

    // 미션을 실행하고 출발지와 목적지를 저장
    public boolean executeMission(Command command) {
        List<MissionItem> missionItems = command.getMissionItems();

        if (missionItems == null || missionItems.isEmpty()) {
            throw new IllegalArgumentException("미션 아이템이 없습니다.");
        }

        // 출발지와 목적지를 설정
        Coordinate startCoordinate = new Coordinate(command.getLatitude(), command.getLongitude());
        Coordinate endCoordinate = new Coordinate(
                missionItems.get(missionItems.size() - 1).getLatitude(),
                missionItems.get(missionItems.size() - 1).getLongitude()
        );

        // 출발지와 목적지를 저장
        startLocations.put(command.getInstanceId(), startCoordinate);
        endLocations.put(command.getInstanceId(), endCoordinate);

        // 로그 출력
        logger.info("드론 인스턴스 ID: {}의 출발지 저장됨 - 위도: {}, 경도: {}",
                command.getInstanceId(), startCoordinate.getLatitude(), startCoordinate.getLongitude());
        logger.info("드론 인스턴스 ID: {}의 도착지 저장됨 - 위도: {}, 경도: {}",
                command.getInstanceId(), endCoordinate.getLatitude(), endCoordinate.getLongitude());

        return true; // 성공적으로 수행 시 true 반환
    }

    // 드론 생성 시 출발지 정보 저장
    public void saveStartLocation(Command command) {
        Coordinate startCoordinate = new Coordinate(command.getLatitude(), command.getLongitude());
        startLocations.put(command.getInstanceId(), startCoordinate);

        // 로그 출력
        logger.info("드론 인스턴스 ID: {}의 출발지 저장됨 - 위도: {}, 경도: {}",
                command.getInstanceId(), startCoordinate.getLatitude(), startCoordinate.getLongitude());
    }

    // 미션 수행 시 도착지 정보 저장
    public void saveEndLocation(Command command) {
        List<MissionItem> missionItems = command.getMissionItems();
        if (missionItems != null && !missionItems.isEmpty()) {
            Coordinate endCoordinate = new Coordinate(
                    missionItems.get(missionItems.size() - 1).getLatitude(),
                    missionItems.get(missionItems.size() - 1).getLongitude()
            );
            endLocations.put(command.getInstanceId(), endCoordinate);

            // 로그 출력
            logger.info("드론 인스턴스 ID: {}의 도착지 저장됨 - 위도: {}, 경도: {}",
                    command.getInstanceId(), endCoordinate.getLatitude(), endCoordinate.getLongitude());
        } else {
            throw new IllegalArgumentException("미션 아이템이 없습니다.");
        }
    }

    // 출발지 정보 조회
    public Coordinate getStartLocation(int instanceId) {
        return startLocations.get(instanceId);
    }

    // 목적지 정보 조회
    public Coordinate getEndLocation(int instanceId) {
        return endLocations.get(instanceId);
    }
}
