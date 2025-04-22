package com.skillbridgebackend.skillBridge.backend.Service;

import com.skillbridgebackend.skillBridge.backend.Dto.FeedbackDto;
import com.skillbridgebackend.skillBridge.backend.Entity.Feedback;

import java.util.List;

public interface FeedbackService {
    FeedbackDto addFeedback(long courseId, FeedbackDto feedbackDto);

    List<FeedbackDto> getFeedbackByCourseId(long courseId);

    FeedbackDto getFeedbackById(long courseId, long feedbackId);

    FeedbackDto updateFeedback(long courseId, long feedbackId, FeedbackDto feedbackDto);

    void deleteFeedback(long courseId, long feedbackId);
}
