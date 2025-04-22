package com.skillbridgebackend.skillBridge.backend.Dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class BuyCourseDto {

    private Long id;
    private String courseName;
    private Date courseBuyTimeStamp;
    private Date courseExpiry;
    private Long courseId;
    private Long userId;

}
