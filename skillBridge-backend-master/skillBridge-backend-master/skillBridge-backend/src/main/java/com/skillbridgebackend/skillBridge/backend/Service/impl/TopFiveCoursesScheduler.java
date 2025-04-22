package com.skillbridgebackend.skillBridge.backend.Service.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillbridgebackend.skillBridge.backend.Dto.TopFiveCoursesDto;
import com.skillbridgebackend.skillBridge.backend.Service.BuyCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TopFiveCoursesScheduler {

    @Autowired
    private final BuyCourseService buyCourseService;

    @Value("${cron.job.enabled:true}")
    private Boolean cronJobEnabled;

    public TopFiveCoursesScheduler(BuyCourseService buyCourseService) {
        this.buyCourseService = buyCourseService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kolkata") // Cron expression to run every 24 hours at midnight UTC
    public void scheduleTopFiveCoursesTask() throws JsonProcessingException {

        if (cronJobEnabled) {
            List<TopFiveCoursesDto> topFiveCourses = buyCourseService.getTopFivePurchasedCourses();
            // You can log the results, store them in a database, or send a report email, etc.
            System.out.println("Top 5 Purchased Courses: " + topFiveCourses);
        }else {
            System.out.println("cron job is made disabled, skipping the execution for that");
        }
    }
}
