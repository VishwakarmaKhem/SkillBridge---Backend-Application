package com.skillbridgebackend.skillBridge.backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "courses", uniqueConstraints = {@UniqueConstraint(columnNames = {"courseName"})}
)

public class Courses implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "courseName", nullable = false)
    private String courseName;

    @Column(name = "coursePrice", nullable = false)
    private double coursePrice;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "courseContent", nullable = false)
    private String courseContent;

    @OneToMany(mappedBy = "courses", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Feedback> feedbacks = new HashSet<>();



}
