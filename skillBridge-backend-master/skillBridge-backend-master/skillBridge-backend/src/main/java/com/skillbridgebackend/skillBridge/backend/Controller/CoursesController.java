package com.skillbridgebackend.skillBridge.backend.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillbridgebackend.skillBridge.backend.Dto.CourseResponse;
import com.skillbridgebackend.skillBridge.backend.Dto.CoursesDto;
import com.skillbridgebackend.skillBridge.backend.Service.CoursesService;
import com.skillbridgebackend.skillBridge.backend.Utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CoursesController {

    private CoursesService coursesService;

    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    //building create course REST API
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CoursesDto> createCourse(@Valid @RequestBody CoursesDto coursesDto){
        return new ResponseEntity<>(coursesService.createCourse(coursesDto), HttpStatus.CREATED);
    }

    //building get course by id
    @GetMapping("/{id}")
    public ResponseEntity<CoursesDto> getCourseById(@PathVariable("id") Long id) throws JsonProcessingException {
        return new ResponseEntity<>(coursesService.getCourseById(id), HttpStatus.OK);
    }

    //building get all courses rest api
    @GetMapping
    public ResponseEntity<CourseResponse> getAllCourses(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(coursesService.getAllCourse(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //building update course rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CoursesDto> updateCourse(@PathVariable("id") Long id,
                                                   @Valid @RequestBody CoursesDto coursesDto){
        return new ResponseEntity<>(coursesService.updateCourseById(id, coursesDto), HttpStatus.OK);
    }

    //building delete course rest api
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable("id") Long id){
        coursesService.deleteCourseById(id);
        return new ResponseEntity<>("Course successfully deleted!", HttpStatus.OK);
    }

}
