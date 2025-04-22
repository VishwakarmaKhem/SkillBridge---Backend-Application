package com.skillbridgebackend.skillBridge.backend.Service.impl;

import com.skillbridgebackend.skillBridge.backend.Dto.FeedbackDto;
import com.skillbridgebackend.skillBridge.backend.Dto.KafkaProducerDto;
import com.skillbridgebackend.skillBridge.backend.Entity.Courses;
import com.skillbridgebackend.skillBridge.backend.Entity.Feedback;
import com.skillbridgebackend.skillBridge.backend.Entity.User;
import com.skillbridgebackend.skillBridge.backend.Exception.SkillBridgeAPIException;
import com.skillbridgebackend.skillBridge.backend.Repository.BuyCourseRepository;
import com.skillbridgebackend.skillBridge.backend.Repository.CoursesRepository;
import com.skillbridgebackend.skillBridge.backend.Repository.FeedbackRepository;
import com.skillbridgebackend.skillBridge.backend.Repository.UserRepository;
import com.skillbridgebackend.skillBridge.backend.Utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeedbackConsumer {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private BuyCourseRepository buyCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "course-feedback", groupId = AppConstants.Group_Id)
    public void feedbackListener(KafkaProducerDto kafkaProducerDto){

        Courses courses = coursesRepository.findById(kafkaProducerDto.getCourseId())
                .orElseThrow(()-> new SkillBridgeAPIException(HttpStatus.NOT_FOUND, "no course found"));

        Feedback feedback = new Feedback();
        feedback.setName(kafkaProducerDto.getName());
        feedback.setEmail(kafkaProducerDto.getEmail());
        feedback.setBody(kafkaProducerDto.getFeedbackBody());
        feedback.setCourses(courses);

        feedbackRepository.save(feedback);

    }
}
