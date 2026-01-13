package org.example.controllers;

import org.example.dtos.ApiResponse;
import org.example.exceptions.BadRequestException;
import org.example.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse enroll(@PathVariable UUID courseId, Authentication authentication){
        UUID studentId=UUID.fromString(authentication.getPrincipal().toString());
        enrollmentService.enroll(studentId,courseId);
        return new ApiResponse(
                true,
                "Enrolled Successfully",
                null
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse myEnrollments(Authentication authentication){
        if(authentication==null){
            throw new BadRequestException("UnAuthorized");
        }
        UUID studentId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Enrolled Courses",
                enrollmentService.getMyEnrolledCourses(studentId)
        );
    }
}

