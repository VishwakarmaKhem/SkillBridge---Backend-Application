package com.skillbridgebackend.skillBridge.backend.Repository;

import com.skillbridgebackend.skillBridge.backend.Entity.Courses;
import com.skillbridgebackend.skillBridge.backend.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByCourses(Courses courses);
}
