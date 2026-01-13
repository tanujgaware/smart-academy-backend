package org.example.services;

import org.example.dtos.CourseResponse;
import org.example.entities.*;
import org.example.exceptions.BadRequestException;
import org.example.repositories.CourseRepository;
import org.example.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{

    @Autowired
    UserService userService;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Override
    public void enroll(UUID studentId,UUID courseId){
        System.out.println(studentId);
        System.out.println(courseId);
        User student = userService.findById(studentId);
        Course course=courseRepository.findById(courseId)
                .orElseThrow(()->new BadRequestException("Course Not Found"));

        if(course.getStatus()!= CourseStatus.PUBLISHED){
            throw new BadRequestException("Course Not Accessible");
        }

        if(enrollmentRepository.existsByUserAndCourse(student,course)){
            throw new BadRequestException("Already Enrolled!!!");
        }

        Enrollment enrollment=new Enrollment();
        enrollment.setCourse(course);
        enrollment.setUser(student);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public List<CourseResponse> getMyEnrolledCourses(UUID studentId){
        User student=userService.findById(studentId);
        return enrollmentRepository.findByUser(student)
                .stream()
                .map(enrollment -> enrollment.getCourse())
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getStatus().name(),
                        course.getSkillsTags()
                                .stream()
                                .map(SkillTag::getName)
                                .collect(Collectors.toSet()),
                        course.getCoverImgUrl()
                ))
                .toList();
    }
}
