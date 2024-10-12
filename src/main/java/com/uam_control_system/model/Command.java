package com.uam_control_system.model;

import lombok.Data;
import java.util.List;

@Data
public class Command {
    private int type; // 명령의 타입 (0: 생성, 1: 삭제, 2: 미션)
    private int instanceId; // 드론 인스턴스 ID
    private double latitude; // 드론 생성 시의 위도
    private double longitude; // 드론 생성 시의 경도
    private List<MissionItem> missionItems; // 미션 아이템 리스트
}
