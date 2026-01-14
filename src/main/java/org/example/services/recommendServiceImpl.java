package org.example.services;


import org.example.dtos.CourseResponse;
import org.example.entities.*;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repositories.CourseRepository;
import org.example.repositories.EnrollmentRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class recommendServiceImpl implements  recommendService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    public List<CourseResponse> recommendv1(UUID userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User Not found"));
        List<Course>publishedCourses=courseRepository.findByStatus(CourseStatus.PUBLISHED);
        List<Course>enrollList=enrollmentRepository.findByUser(user).stream().map((ele)->ele.getCourse()).toList();

        List<String>userSkills=enrollList.stream().flatMap(course -> course.getSkillsTags().stream())
                .map(SkillTag::getName)
                .distinct()
                .toList();

        return publishedCourses.stream()
                .filter(course->enrollList.stream().noneMatch(ele->ele.getId().equals(course.getId())))
                .filter(course->course.getSkillsTags().stream()
                        .map(SkillTag::getName)
                        .anyMatch(userSkills::contains))
                .sorted((c1, c2) -> {
                    long c1Matches = c1.getSkillsTags().stream()
                            .map(SkillTag::getName)
                            .filter(userSkills::contains)
                            .count();

                    long c2Matches = c2.getSkillsTags().stream()
                            .map(SkillTag::getName)
                            .filter(userSkills::contains)
                            .count();

                    return Long.compare(c2Matches, c1Matches);
                })
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getStatus().name(),
                        course.getSkillsTags()
                                .stream()
                                .map(SkillTag::getName)
                                .collect(java.util.stream.Collectors.toSet()),
                        course.getCoverImgUrl()
                ))
                .toList();
    }

//    public List<CourseResponse> recommendv2(UUID userId){
//
//    }
}
