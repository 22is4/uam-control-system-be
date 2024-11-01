package com.uam_control_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

public class Path {
    private int instanceId; // 경로를 따르는 드론 ID
    private List<PathCoordinate> coordinates; // 경로를 구성하는 좌표 리스트
}
