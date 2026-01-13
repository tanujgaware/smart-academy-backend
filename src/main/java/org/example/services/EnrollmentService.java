package org.example.services;

import org.example.dtos.CourseResponse;
import org.example.entities.Course;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    void enroll(UUID studentId,UUID courseId);
    List<CourseResponse> getMyEnrolledCourses(UUID studentId);
}
