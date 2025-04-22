package com.skillbridgebackend.skillBridge.backend.Repository;

import com.skillbridgebackend.skillBridge.backend.Entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long> {

}
