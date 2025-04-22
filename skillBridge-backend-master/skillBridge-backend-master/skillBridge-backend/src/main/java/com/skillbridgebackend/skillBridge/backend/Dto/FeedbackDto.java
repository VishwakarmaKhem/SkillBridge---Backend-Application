package com.skillbridgebackend.skillBridge.backend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackDto {
    private long id;

//    @NotEmpty(message = "name should have at least two characters")
    private String name;

//    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    @NotEmpty(message = "Body should not be null or empty")
    private String body;
}
