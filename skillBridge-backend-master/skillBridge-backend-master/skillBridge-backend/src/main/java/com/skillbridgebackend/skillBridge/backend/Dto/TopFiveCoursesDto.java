package com.skillbridgebackend.skillBridge.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopFiveCoursesDto {
    private String courseName;
    private Long dailyBuyCount;
}
