package com.skillbridgebackend.skillBridge.backend.Controller;

import com.skillbridgebackend.skillBridge.backend.Dto.FeedbackDto;
import com.skillbridgebackend.skillBridge.backend.Entity.User;
import com.skillbridgebackend.skillBridge.backend.Service.FeedbackService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@Slf4j
public class FeedbackController {

    private FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    //building add feedback rest api
    @PostMapping("/courses/{courseId}/feedbacks")
    public ResponseEntity<FeedbackDto> addFeedback(@PathVariable("courseId") long courseId,
                                                   @Valid @RequestBody FeedbackDto feedbackDto
    ){
        return new ResponseEntity<>(feedbackService.addFeedback(courseId, feedbackDto), HttpStatus.CREATED);
    }

    @GetMapping("/courses/{courseId}/feedbacks")
    public List<FeedbackDto> getFeedbacksByCourseId(@PathVariable(value = "courseId") Long courseId){
        return feedbackService.getFeedbackByCourseId(courseId);
    }

    //building get feedback by id
    @GetMapping("/courses/{courseId}/feedbacks/{id}")
    public ResponseEntity<FeedbackDto> getFeedbackById(@PathVariable("courseId") long courseId,
                                                       @PathVariable("id") long feedbackId){
        return new ResponseEntity<>(feedbackService.getFeedbackById(courseId, feedbackId), HttpStatus.OK);
    }

//    building update feedback rest api
    @PutMapping("/courses/{courseId}/feedbacks/{id}")
    public ResponseEntity<FeedbackDto> updateFeedback(@PathVariable("courseId") long courseId,
                                                      @PathVariable("id") long feedbackId,
                                                     @Valid @RequestBody FeedbackDto feedbackDto){
        return new ResponseEntity<>(feedbackService.updateFeedback(courseId, feedbackId, feedbackDto), HttpStatus.OK);
    }

    //building delete feedback rest api
    @DeleteMapping("/courses/{courseId}/feedbacks/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable("courseId") long courseId,
                                                 @PathVariable("id") long feedbackId){
        feedbackService.deleteFeedback(courseId, feedbackId);
        return new ResponseEntity<>("Feedback deleted Successfully", HttpStatus.OK);
    }
}
