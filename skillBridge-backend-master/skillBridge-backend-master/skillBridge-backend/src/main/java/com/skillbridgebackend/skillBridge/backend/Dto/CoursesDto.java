package com.skillbridgebackend.skillBridge.backend.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CoursesDto {
    private Long id;

    @NotEmpty
    @Size(min = 5, message = "Course name should have 5 characters")
    private String courseName;

    @Min(value = 0, message = "Course price cannot be empty, Note : you can add zero")
    private double coursePrice;

    @NotEmpty
    @Size(min = 10, message = "Course description should contain at least 10 characters")
    private String description;

    @NotEmpty
    @Size(min = 20, message = "Course content should contain at least 20 characters")
    private String courseContent;

    private Set<FeedbackDto> feedbacks;

}
