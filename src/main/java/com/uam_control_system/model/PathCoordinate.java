package com.uam_control_system.model;

import lombok.Data;

@Data
public class PathCoordinate {
    private double latitude; // 위도
    private double longitude; // 경도
    private double altitude; // 고도
}
