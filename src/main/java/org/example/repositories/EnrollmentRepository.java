package org.example.repositories;

import org.example.dtos.CourseResponse;
import org.example.entities.Course;
import org.example.entities.Enrollment;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    List<Enrollment> findByUser(User student);
    boolean existsByUserAndCourse(User student, Course course);
}
