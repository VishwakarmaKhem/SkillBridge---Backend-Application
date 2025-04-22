package com.skillbridgebackend.skillBridge.backend.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillbridgebackend.skillBridge.backend.Dto.BuyCourseDto;
import com.skillbridgebackend.skillBridge.backend.Dto.TopFiveCoursesDto;
import com.skillbridgebackend.skillBridge.backend.Repository.BuyCourseRepository;
import com.skillbridgebackend.skillBridge.backend.Service.BuyCourseService;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-courses")
public class BuyCourseController {
    private final BuyCourseService buyCourseService;

    public BuyCourseController(BuyCourseService buyCourseService, BuyCourseRepository buyCourseRepository) {
        this.buyCourseService = buyCourseService;
    }

    @PostMapping("/buy")
    public ResponseEntity<String> purchaseCourse(@RequestParam Long userId,
                                                 @RequestParam Long courseId){
        try {
            buyCourseService.buyCourse(userId, courseId);
            return new ResponseEntity<>("your successfully purchased this course", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException("Error while purchasing the course: "+e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BuyCourseDto>> allPurchasedCourses(){
        return new ResponseEntity<>(buyCourseService.getAllPurchasedCourse(), HttpStatus.OK);
    }

    @GetMapping("/top-5")
    public ResponseEntity<List<TopFiveCoursesDto>> getTop5PurchasedCourses() throws JsonProcessingException {
        List<TopFiveCoursesDto> topCourses = buyCourseService.getTopFivePurchasedCourses();
        return new ResponseEntity<>(topCourses, HttpStatus.OK);
    }
}
