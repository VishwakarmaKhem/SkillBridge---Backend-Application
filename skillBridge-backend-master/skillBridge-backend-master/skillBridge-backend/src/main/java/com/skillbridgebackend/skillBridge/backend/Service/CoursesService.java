package com.skillbridgebackend.skillBridge.backend.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillbridgebackend.skillBridge.backend.Dto.CourseResponse;
import com.skillbridgebackend.skillBridge.backend.Dto.CoursesDto;

import java.util.List;

public interface CoursesService {
    CoursesDto createCourse(CoursesDto coursesDto);

    CoursesDto getCourseById(Long id) throws JsonProcessingException;

    CourseResponse getAllCourse(int pageNo, int pageSize, String sortBy, String sortDir);

    CoursesDto updateCourseById(Long id, CoursesDto coursesDto);

    void deleteCourseById(Long id);

}
