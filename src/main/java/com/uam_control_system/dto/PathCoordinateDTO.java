package com.uam_control_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PathCoordinateDTO {
    private double latitude;
    private double longitude;
    private double altitude;
}