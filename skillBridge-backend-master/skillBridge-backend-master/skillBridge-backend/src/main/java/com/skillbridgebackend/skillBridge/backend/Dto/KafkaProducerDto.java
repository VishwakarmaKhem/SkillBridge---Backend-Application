package com.skillbridgebackend.skillBridge.backend.Dto;

import com.skillbridgebackend.skillBridge.backend.Entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaProducerDto implements Serializable {
    private Long courseId;
    private Long userId;
    private String feedbackBody;
    private String name;
    private String email;
    private Boolean hasPurchased;
}
