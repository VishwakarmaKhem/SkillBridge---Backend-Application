package com.skillbridgebackend.skillBridge.backend.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillbridgebackend.skillBridge.backend.Dto.BuyCourseDto;
import com.skillbridgebackend.skillBridge.backend.Dto.TopFiveCoursesDto;
import com.skillbridgebackend.skillBridge.backend.Entity.BuyCourse;

import java.util.List;

public interface BuyCourseService {
    BuyCourse buyCourse(Long userId, Long courseId);

    List<BuyCourseDto> getAllPurchasedCourse();

    List<TopFiveCoursesDto> getTopFivePurchasedCourses() throws JsonProcessingException;
}