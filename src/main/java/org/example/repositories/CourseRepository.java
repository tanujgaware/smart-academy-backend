package org.example.repositories;

import org.example.dtos.CourseResponse;
import org.example.entities.Course;
import org.example.entities.CourseStatus;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
//    Optional<Course> findById(String id);
    List<Course> findByInstructor(User instructor);
    List<Course> findByStatus(CourseStatus status);
}
